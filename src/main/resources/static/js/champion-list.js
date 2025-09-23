document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("champion-search");
    const buttons = document.querySelectorAll(".pos-btn");
    const tiles = document.querySelectorAll(".tile");

    let activePos = "All";

    // 검색
    searchInput.addEventListener("input", () => {
        filterByName();
    });

    // 포지션 필터 버튼
    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            buttons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            activePos = btn.dataset.pos;
            filterByPosition();
        });
    });

    function filterByName() {
        const query = searchInput.value.toLowerCase();

        tiles.forEach(tile => {
            console.log(`name : ${tile.dataset.name}, pos : ${tile.dataset.position}`)
            const name = tile.dataset.name.toLowerCase();
            const matchName = name.includes(query);

            tile.style.display = (matchName) ? "flex" : "none";
        });
    }

    function filterByPosition() {
        const cards = document.querySelectorAll("#champion-grid .tile");

        cards.forEach(card => {
            const positions = (card.dataset.position || "").split(",").map(s => s.trim()).filter(Boolean);
            const show = (activePos === "All") || positions.includes(activePos);

            card.style.display = show ? "" : "none";
        })
    }

});