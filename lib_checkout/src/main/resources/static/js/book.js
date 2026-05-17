document.addEventListener("DOMContentLoaded", () => {
    const isbnInput = document.querySelector("input[name='isbn']");
    if (isbnInput instanceof HTMLInputElement) {
        isbnInput.placeholder = "예: 9781234567890";
    }
});
