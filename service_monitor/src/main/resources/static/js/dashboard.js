document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll("[data-tab-button]");
    const panels = document.querySelectorAll("[data-tab-panel]");
    const panelNames = new Set(Array.from(panels, (panel) => panel.dataset.tabPanel));

    const normalizeHash = () => window.location.hash.replace(/^#tab-/, "");

    const activateTab = (name) => {
        if (!panelNames.has(name)) {
            return;
        }

        buttons.forEach((button) => {
            button.classList.toggle("is-active", button.dataset.tabButton === name);
            button.setAttribute("aria-selected", String(button.dataset.tabButton === name));
        });

        panels.forEach((panel) => {
            const isActive = panel.dataset.tabPanel === name;
            panel.classList.toggle("is-active", isActive);
            panel.setAttribute("aria-hidden", String(!isActive));
        });
    };

    activateTab(panelNames.has(normalizeHash()) ? normalizeHash() : "overview");
    window.addEventListener("hashchange", () => activateTab(panelNames.has(normalizeHash()) ? normalizeHash() : "overview"));
});
