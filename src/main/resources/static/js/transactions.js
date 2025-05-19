// Wait for the entire DOM to be fully loaded before executing the script
document.addEventListener("DOMContentLoaded", () => {
    // Select the form element with class 'filter-form'
    const form = document.querySelector(".filter-form");
    // Select all <input> and <select> elements inside the form
    const inputs = form.querySelectorAll("input, select");

    // Loop through each input and select element
    inputs.forEach(input => {
        // Attach a keydown listener to each input/select
        input.addEventListener("keydown", (e) => {
            // If the pressed key is 'Enter'
            if (e.key === "Enter") {
                // Submit the form
                form.submit();
            }
        });
    });
});
