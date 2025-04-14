document.addEventListener("DOMContentLoaded", function () {
    const tanksContainer = document.getElementById("tanksContainer");
    const tankCards = Array.from(tanksContainer.getElementsByClassName("offer"));
    const paginationContainer = document.getElementById("pagination");

    const itemsPerPage = 5;
    let currentPage = 1;
    const totalPages = Math.ceil(tankCards.length / itemsPerPage);

    function renderPage(page) {
        currentPage = page;
        tankCards.forEach(card => card.style.display = "none");

        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        tankCards.slice(start, end).forEach(card => card.style.display = "block");

        renderPagination();
    }

    function renderPagination() {
        paginationContainer.innerHTML = "";
        if (totalPages <= 1) return; // No pagination needed if only one page exists

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "Previous";
        prevBtn.disabled = (currentPage === 1);
        prevBtn.addEventListener("click", () => {
            if (currentPage > 1) renderPage(currentPage - 1);
        });
        paginationContainer.appendChild(prevBtn);

        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = document.createElement("button");
            pageBtn.textContent = i;
            if (i === currentPage) pageBtn.classList.add("active");
            pageBtn.addEventListener("click", (e) => {
                renderPage(Number(e.target.textContent));
            });
            paginationContainer.appendChild(pageBtn);
        }

        const nextBtn = document.createElement("button");
        nextBtn.textContent = "Next";
        nextBtn.disabled = (currentPage === totalPages);
        nextBtn.addEventListener("click", () => {
            if (currentPage < totalPages) renderPage(currentPage + 1);
        });
        paginationContainer.appendChild(nextBtn);
    }

    renderPage(1);
});
