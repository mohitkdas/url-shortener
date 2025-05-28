async function shortenUrl() {
    const longUrl = document.getElementById("longUrl").value.trim();
    const resultDiv = document.getElementById("result");
    resultDiv.innerHTML = "Processing...";

    try {
        const response = await fetch("/api/v1/shorten", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ longUrl })
        });

        if (!response.ok) {
            throw new Error("Server error");
        }

        const shortUrl = await response.text();
        resultDiv.innerHTML = `Short URL: <a href="${shortUrl}" target="_blank">${shortUrl}</a>`;
    } catch (error) {
        resultDiv.innerHTML = "Error shortening URL. Try again.";
        console.error(error);
    }
}