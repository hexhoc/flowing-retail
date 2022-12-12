const gatewayHost = 'http://localhost:8072';
let count = 0;
let orderJson = {
    "orderId": null,
    "customer": {
        "id": "e8f5cc5a-af5a-4a94-a08c-fe22190aa036",
        "name": "John Tompson",
        "address": "Germany, sesame street, 21"
    },
    "items": [
        {
            "articleId": "article_1",
            "amount": 5
        },
        {
            "articleId": "article_2",
            "amount": 10
        }
    ]
}

function getTest() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", gatewayHost + '/orderservice/api/v1/order/test', true);
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

    xhr.open("POST", gatewayHost + '/orderservice/api/v1/order')
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