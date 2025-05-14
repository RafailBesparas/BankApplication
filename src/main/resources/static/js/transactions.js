document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".filter-form");
    const inputs = form.querySelectorAll("input, select");

    inputs.forEach(input => {
        input.addEventListener("keydown", (e) => {
            if (e.key === "Enter") {
                form.submit();
            }
        });
    });
});
