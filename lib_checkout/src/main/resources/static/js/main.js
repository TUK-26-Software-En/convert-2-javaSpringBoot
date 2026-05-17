document.addEventListener("DOMContentLoaded", () => {
    const page = document.body.dataset.page;
    document.querySelectorAll(".main-nav a").forEach((link) => {
        if (page && link.getAttribute("href")?.includes(page)) {
            link.classList.add("is-active");
        }
        if (page === "home" && link.getAttribute("href") === "/") {
            link.classList.add("is-active");
        }
    });

    const focusTarget = document.querySelector("[data-focus]");
    if (focusTarget instanceof HTMLElement) {
        focusTarget.focus();
    }
});
