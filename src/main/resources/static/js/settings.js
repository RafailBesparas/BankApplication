// Wait for the entire HTML document to be fully parsed and ready
document.addEventListener("DOMContentLoaded", () => {
    // Select the form element that has the class 'settings-form'
    const form = document.querySelector(".settings-form");
    // Within the form, select the input field named 'timeZone'
    const tzInput = form.querySelector("[name='timeZone']");
    // If the input exists and its value is currently empty
    if (tzInput && !tzInput.value) {
     // Automatically fill the input with the user's current time zone
     // For example, 'Europe/Berlin' or 'America/New_York'
        tzInput.value = Intl.DateTimeFormat().resolvedOptions().timeZone;
    }
});