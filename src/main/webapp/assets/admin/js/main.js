// Xử lý xem trước danh sách nhiều ảnh khi chọn Upload BĐS (Phục vụ Mục A.1)
document.addEventListener("DOMContentLoaded", function () {
    const imageInput = document.getElementById("bdsImageInput");
    const previewContainer = document.getElementById("imagePreviewContainer");

    if (imageInput && previewContainer) {
        imageInput.addEventListener("change", function (event) {
            previewContainer.innerHTML = ""; // Xóa các ảnh preview cũ
            const files = event.target.files;

            if (files) {
                Array.from(files).forEach(file => {
                    if (file.type.startsWith("image/")) {
                        const reader = new FileReader();

                        reader.onload = function (e) {
                            const itemDiv = document.createElement("div");
                            itemDiv.className = "image-preview-item";

                            const img = document.createElement("img");
                            img.src = e.target.result;

                            itemDiv.appendChild(img);
                            previewContainer.appendChild(itemDiv);
                        };

                        reader.readAsDataURL(file);
                    }
                });
            }
        });
    }
});