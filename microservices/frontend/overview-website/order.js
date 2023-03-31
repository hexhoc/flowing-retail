const gatewayHost = 'http://localhost:3000';
let count = 0;
let orderJson = {
    "id": null,
    "customerId": 1,
    "address": "",
    "status": null,
    "is_deleted": null,
    "created_date": null,
    "modified_date": null,
    "version": null,
    "items": [
        {
            "id": null,
            "productId": 1,
            "quantity": 1,
            "price": 1799.00
        },
        {
            "id": null,
            "productId": 2,
            "quantity": 1,
            "price": 899.00
        }
    ]
}

function getTest() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", gatewayHost + '/api/v1/order/test', true);
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) { // анализируем HTTP-статус ответа, если статус не 200, то произошла ошибка
            alert(`Error ${xhr.status}: ${xhr.statusText}`); // Например, 404: Not Found
        } else { // если всё прошло гладко, выводим результат
            alert(`Success get ${xhr.response.length} byte`); // response -- это ответ сервера
        }
    };
}

function placeOrder() {
    let xhr = new XMLHttpRequest();

    let json = JSON.stringify(orderJson);

    xhr.open("POST", gatewayHost + '/api/v1/order')
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');

    xhr.send(json);

    xhr.onload = function() {
        if (xhr.status != 200) { // анализируем HTTP-статус ответа, если статус не 200, то произошла ошибка
            alert(`Error ${xhr.status}: ${xhr.statusText}`); // Например, 404: Not Found
        } else { // если всё прошло гладко, выводим результат
            count = count + 1;
            document.getElementById('thanks').textContent = 'Thank you for order #' + count + '.<br>Order id: <b>' + JSON.parse(xhr.response).orderId + '</b>';
            alert(`Success, get ${xhr.response.length} bite`); // response -- это ответ сервера
        }
    };
}