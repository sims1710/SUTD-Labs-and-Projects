// update the ol with id = "deptsregion" in the current page
// input: json contains list of departments
// output: none
function update_deptsregion(json) {
    var html = "";
    for (let i = 0; i < json.length; i++) {
        const dept = json[i];
        html += `<li><div>${dept.code}</div></li>`;
    }
    var region = document.getElementById("deptsregion");
    region.innerHTML = html;
}

// handle the click event of the "SendButton"
// invoke /dept/submit/ endpoint to create the department
// Task 1 TODO:
// render the API result (all staff) in the deptsregion div
function handleSendButtonClick() {
    var code = document.getElementById("code").value;
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            var res = xhr.responseText;
            var json = JSON.parse(res);
            update_deptsregion(json);
        }
    };
    // constructing an HTTP POST request
    var params = `code=${code}`;
    xhr.open('POST', `/dept/submit/`, true);
    // Send the proper header information along with the request
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send(params);
}

// set up the event listener for the send button
// call /dept/all to get the current list of departments
function run() {
    var sendButton = document.getElementById("sendButton");
    sendButton.addEventListener("click", handleSendButtonClick);
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            var res = xhr.responseText;
            var json = JSON.parse(res);
            update_deptsregion(json);
        }
    };

    xhr.open('GET', `/dept/all`);
    xhr.send();
}

document.addEventListener("DOMContentLoaded", run);

