// Fetch currencies and populate the dropdowns
window.onload = function() {
    const apiUrl = "https://api.currencybeacon.com/v1/latest?api_key=1SMxQumaAcC996SUXGsnlBPW44t2RR82";

    axios.get(apiUrl)
      .then(response => {
        const currencies = Object.keys(response.data.response.rates);
        const sourceCurrencyDropdown = document.getElementById("sourceCurrency");
        const targetCurrencyDropdown = document.getElementById("targetCurrency");
  
        // Populate source and target currency dropdowns
        currencies.forEach(currency => {
          const option = document.createElement("option");
          option.value = currency;
          option.textContent = currency;
          sourceCurrencyDropdown.appendChild(option);
          targetCurrencyDropdown.appendChild(option.cloneNode(true));
        });
      })
      .catch(error => {
        console.error("Error fetching currency data:", error);
        alert("Failed to load currencies.");
      });
  };
  
// Event listener for the Convert button
document.getElementById("convertBtn").addEventListener("click", function() {
  const sourceCurrency = document.getElementById("sourceCurrency").value;
  const targetCurrency = document.getElementById("targetCurrency").value;
  const amount = document.getElementById("amount").value;

  if (!amount || amount <= 0) {
    alert("Please enter a valid amount");
    return;
  }

  const apiUrl = `http://localhost:8080/currency/convert`;

  // Make an API request using Axios
  axios.post(apiUrl, {
    sourceCurrency: sourceCurrency,
    targetCurrency: targetCurrency,
    amount: amount
  })
  .then(response => {
    const formattedAmount = Number(amount).toLocaleString();
    const convertedAmount = Number(response.data.convertedAmount).toLocaleString();

    document.getElementById("targetCurrencyResult").innerText = targetCurrency;
    document.getElementById("sourceCurrencyResult").innerText = sourceCurrency;
    document.getElementById("amountInput").innerText = formattedAmount;
    document.getElementById("resultAmount").innerText = convertedAmount;

    document.getElementById("result").style.display = "block";
  })
    .catch(error => {
      console.error("Error while converting currency:", error);
      alert("An error occurred while converting the currency. Please try again.");
    });
  });
