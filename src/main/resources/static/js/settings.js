document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".settings-form");
    const tzInput = form.querySelector("[name='timeZone']");
    if (tzInput && !tzInput.value) {
        tzInput.value = Intl.DateTimeFormat().resolvedOptions().timeZone;
    }
});