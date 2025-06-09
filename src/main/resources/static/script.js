document
  .getElementById("exchangeForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    const from = document.getElementById("fromCurrency").value.toUpperCase();
    const to = document.getElementById("toCurrency").value.toUpperCase();
    const amount = parseFloat(document.getElementById("amount").value);
    const resultsDiv = document.getElementById("results");
    const convertButton = document.querySelector('button[type="submit"]'); // Get the button element

    // --- NEW: Debouncing/Throttling mechanism ---
    // Disable the button immediately to prevent further clicks
    convertButton.disabled = true;
    resultsDiv.innerHTML = "<p>Loading...</p>"; // Show loading message right away

    // Re-enable the button after a delay (e.g., 2 seconds)
    // This gives the API call time to process and prevents rapid re-submission
    setTimeout(() => {
      convertButton.disabled = false;
    }, 2000); // Re-enable after 2000 milliseconds (2 seconds)
    // --- END NEW ---

    if (isNaN(amount) || amount <= 0) {
      resultsDiv.innerHTML =
        '<p class="error">Please enter a valid amount.</p>';
      convertButton.disabled = false; // Re-enable if validation fails instantly
      return;
    }

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
      // In case of an API error, ensure the button is re-enabled so user can retry
      convertButton.disabled = false;
    }
  });