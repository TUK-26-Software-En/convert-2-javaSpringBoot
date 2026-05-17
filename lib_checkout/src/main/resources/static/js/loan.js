document.addEventListener("DOMContentLoaded", () => {
    const dueDateInput = document.querySelector("input[name='dueDate']");
    if (dueDateInput instanceof HTMLInputElement && !dueDateInput.value) {
        const today = new Date();
        today.setDate(today.getDate() + 14);
        dueDateInput.value = today.toISOString().slice(0, 10);
    }
});
