document.addEventListener("DOMContentLoaded", () => {
    const phoneInput = document.querySelector("input[name='phoneNumber']");
    if (phoneInput instanceof HTMLInputElement) {
        phoneInput.placeholder = "010-0000-0000";
    }
});
