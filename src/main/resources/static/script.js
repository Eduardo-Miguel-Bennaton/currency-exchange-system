document
  .getElementById("exchangeForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();

    const from = document.getElementById("fromCurrency").value.toUpperCase();
    const to = document.getElementById("toCurrency").value.toUpperCase();
    const amount = parseFloat(document.getElementById("amount").value);
    const resultsDiv = document.getElementById("results");
    const convertButton = document.querySelector('button[type="submit"]');

    convertButton.disabled = true;
    resultsDiv.innerHTML = "<p>Loading...</p>";

    setTimeout(() => {
      convertButton.disabled = false;
    }, 2000);

    if (isNaN(amount) || amount <= 0) {
      resultsDiv.innerHTML =
        '<p class="error">Please enter a valid amount.</p>';
      convertButton.disabled = false;
      return;
    }

    try {
      const apiUrl = `/api/exchange?from=${from}&to=${to}&amount=${amount}`;

      const response = await fetch(apiUrl);

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`API_ERROR_STATUS:${response.status} - ${errorText}`);
      }

      const data = await response.json();

      resultsDiv.innerHTML = `
                        <p><strong>From:</strong> ${data.from}</p>
                        <p><strong>To:</strong> ${data.to}</p>
                        <p><strong>Original Amount:</strong> ${data.originalAmount.toFixed(2)}</p>
                        <p><strong>Converted Amount:</strong> ${data.convertedAmount.toFixed(2)}</p>
                        <p><strong>Exchange Rate:</strong> ${data.rate.toFixed(6)}</p>
                        <p><strong>Timestamp:</strong> ${new Date(data.timestamp).toLocaleString()}</p>
                    `;
    } catch (error) {
      console.error("Fetch error:", error);

      let userFriendlyMessage =
        "An unexpected error occurred. Please try again.";

      if (error.message.startsWith("API_ERROR_STATUS:")) {
        const parts = error.message.split(" - ");
        const statusCodeStr = parts[0].replace("API_ERROR_STATUS:", "");
        const statusCode = parseInt(statusCodeStr);
        const backendMessage = parts.slice(1).join(" - ");

        switch (statusCode) {
          case 400:
            userFriendlyMessage =
              "The currency codes or amount you entered are invalid. Please check your input.";
            break;
          case 502:
            if (backendMessage.includes("missing expected exchange rates")) {
              userFriendlyMessage =
                "We couldn't find exchange rates for the currencies you entered. Please ensure they are valid (e.g., USD, EUR).";
            } else {
              userFriendlyMessage =
                "There was an issue processing the currency exchange due to unexpected data. Please try again.";
            }
            break;
          case 503:
            userFriendlyMessage =
              "Our currency exchange service is temporarily unavailable. Please try again in a moment.";
            break;
          case 500:
            userFriendlyMessage =
              "Something went wrong on our side. Please try again later.";
            break;
          default:
            userFriendlyMessage =
              "An error occurred with the currency exchange service. Please try again.";
            break;
        }
      } else if (error.message.includes("Failed to fetch")) {
        userFriendlyMessage =
          "Could not connect to the currency exchange service. Please check your internet connection or ensure the server is running.";
      }

      resultsDiv.innerHTML = `<p class="error">${userFriendlyMessage}</p>`;
      convertButton.disabled = false;
    }
  });
