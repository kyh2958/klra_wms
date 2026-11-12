(function ($) {
    $.fn.SignPad = function (options) {

        const settings = $.extend({
            width: 100,
            height: 200,
            lineColor: '#0000FF',
            lineWidth: 2,
            userId: null,
            canvasId: 'signature-pad',
            onSave: async () => {},
            styles: {
                clearBtn: "btn",
                undoBtn: "btn",
                saveBtn: "btn"
            }
        }, options);

        let undoStack = [];
        
        let hasSignature = false;

        this.html(`
            <div class="signature-container">
                <h3 style="text-align:center; position:absolute; margin:20px;" class="text-dark">서명</h3>
                <canvas id="${settings.canvasId}" style="width:${settings.width}%;height:${settings.height}px;"></canvas>
                <div class="buttons">
                    <button class="${settings.styles.clearBtn}" id="close">닫기</button>
                    <button class="${settings.styles.undoBtn}" id="clear">재서명</button>
                    <button class="${settings.styles.saveBtn}" id="save">저장</button>
                </div>
            </div>
        `);

        const canvas = this.find('canvas')[0];
        const ctx = canvas.getContext('2d');

        /* ===============================
           Canvas DPI 보정 (핵심)
        =============================== */
        const dpr = window.devicePixelRatio || 1;
        const rect = canvas.getBoundingClientRect();

        canvas.width  = rect.width  * dpr;
        canvas.height = rect.height * dpr;

        ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

        ctx.strokeStyle = settings.lineColor;
        ctx.lineWidth = settings.lineWidth;
        ctx.lineJoin = 'round';
        ctx.lineCap = 'round';

        let isDrawing = false;

        /* ===============================
           좌표 계산 (CSS + DPR 보정)
        =============================== */
        function getPoint(event) {
            event.preventDefault();

            const rect = canvas.getBoundingClientRect();
            const scaleX = canvas.width / rect.width;
            const scaleY = canvas.height / rect.height;

            let clientX, clientY;

            if (event.touches && event.touches.length > 0) {
                clientX = event.touches[0].clientX;
                clientY = event.touches[0].clientY;
            } else {
                clientX = event.clientX;
                clientY = event.clientY;
            }

            return {
                x: (clientX - rect.left) * scaleX / dpr,
                y: (clientY - rect.top)  * scaleY / dpr
            };
        }

        function startDrawing(e) {
            isDrawing = true;
            hasSignature = true;   // ✅ 서명 시작됨

            const { x, y } = getPoint(e);
            ctx.beginPath();
            ctx.moveTo(x, y);

            // undo 스택
            undoStack.push(canvas.toDataURL());
        }

        function draw(e) {
            if (!isDrawing) return;
            const { x, y } = getPoint(e);
            ctx.lineTo(x, y);
            ctx.stroke();
        }

        function stopDrawing() {
            isDrawing = false;
            ctx.closePath();
        }

        /* ===============================
           이벤트 바인딩
        =============================== */
        canvas.addEventListener('mousedown', startDrawing);
        canvas.addEventListener('mousemove', draw);
        canvas.addEventListener('mouseup', stopDrawing);
        canvas.addEventListener('mouseleave', stopDrawing);

        canvas.addEventListener('touchstart', startDrawing, { passive: false });
        canvas.addEventListener('touchmove', draw, { passive: false });
        canvas.addEventListener('touchend', stopDrawing);

        /* ===============================
           버튼 기능
        =============================== */
        $('#undo').on('click', () => {
            if (!undoStack.length) return;
            const img = new Image();
            img.src = undoStack.pop();
            img.onload = () => {
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                ctx.drawImage(img, 0, 0, rect.width, rect.height);
            };
        });

        $('#clear').on('click', () => {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            undoStack = [];
            hasSignature = false;  // ✅ 서명 없음
        });

        $('#save').on('click', () => {
			
			if (!hasSignature) {
		        alert('서명이 없습니다.');
		        return;
		    }
		    
            const postData = {
                userId: settings.userId,
                signature: canvas.toDataURL('image/png'),
                timestamp: new Date().toISOString()
            };

            settings.onSave(postData);

            fetch('/save-signature', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            });
        });

        return this;
    };
})(jQuery);
