document
  .getElementById("exchangeForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    const from = document.getElementById("fromCurrency").value.toUpperCase();
    const to = document.getElementById("toCurrency").value.toUpperCase();
    const amount = parseFloat(document.getElementById("amount").value);
    const resultsDiv = document.getElementById("results");

    if (isNaN(amount) || amount <= 0) {
      resultsDiv.innerHTML =
        '<p class="error">Please enter a valid amount.</p>';
      return;
    }

    resultsDiv.innerHTML = "<p>Loading...</p>"; // Show loading message

    try {
      // Your Spring Boot API endpoint
      const apiUrl = `/api/exchange?from=${from}&to=${to}&amount=${amount}`;

      const response = await fetch(apiUrl);

      if (!response.ok) {
        const errorText = await response.text(); // Get raw error message from backend
        throw new Error(
          `API error: ${response.status} ${response.statusText} - ${errorText}`,
        );
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
      resultsDiv.innerHTML = `<p class="error">Error: ${error.message}</p>`;
    }
  }
);
