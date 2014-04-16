var PreloadImg = new Image();
PreloadImg.src = "/images/loading.gif";

function ViewImage(obj, link) {
    //Load the loading images first
    var LoadingImg = new Image();
    LoadingImg.src = "/images/loading.gif";

    obj.src = LoadingImg.src;
    obj.onclick = null;
    //obj.onclick = new Function("fnDisplay_Computers('" + alines[i] + "')");
    

    //Load the image
    var TargetImg = new Image();
    TargetImg.src = link;

    obj.src = TargetImg.src;
}

function ViewThread(messageid, forum_type, page) {
    if (page == 0) {
    } else if (page > 0) {
        document.location.href = "view.aspx?message=" + messageid + "&type=" + forum_type + "&page=" + page;
    } else {
        document.location.href = "topics.aspx?type=" + page;
    }
}

function ViewTopic(forum_type, page) {
    document.location.href = "topics.aspx?type=" + forum_type + "&page=" + page;
}

var xmlHttp;
function BookmarkIt(title, messageid) {
    //sendRequest('http://m.hkgolden.com/AddBookmarkLink.aspx?bid=0&title=' + title + '&messageid=' + messageid, handleBookmarkItRequest);
    
    xmlHttp = GetXmlHttpObject();
    if (xmlHttp == null) {
        alert("Your browser does not support AJAX!");
        return;
    }

    var url = "AddBookmarkLink.aspx?bid=0&title=" + title + "&messageid=" + messageid;
    xmlHttp.onreadystatechange = stateChanged;
    xmlHttp.open("POST", url, true);
    xmlHttp.send(null);
}
function stateChanged() {
    if (xmlHttp.readyState == 4) {
        alert("Add bookmark successful");
    }
}
function GetXmlHttpObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        try {
            return new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    else {
        alert("Your Browser Sucks!\nIt's about time to upgrade don't you think?");
        return null;
    }
}

function handleBookmarkItRequest(req) {
    alert('Added to your bookmark list!');
}



function sendRequest(url, callback, postData) {
    var req = createXMLHTTPObject();
    if (!req) return;
    var method = (postData) ? "POST" : "GET";
    req.open(method, url, true);
    req.setRequestHeader('User-Agent', 'XMLHTTP/1.0');
    if (postData)
        req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    req.onreadystatechange = function () {
        if (req.readyState != 4) return;
        if (req.status != 200 && req.status != 304) {
            //			alert('HTTP error ' + req.status);
            return;
        }
        callback(req);
    }
    if (req.readyState == 4) return;
    req.send(postData);
}

var XMLHttpFactories = [
	function () { return new XMLHttpRequest() },
	function () { return new ActiveXObject("Msxml2.XMLHTTP") },
	function () { return new ActiveXObject("Msxml3.XMLHTTP") },
	function () { return new ActiveXObject("Microsoft.XMLHTTP") }
];

function createXMLHTTPObject() {
    var xmlhttp = false;
    for (var i = 0; i < XMLHttpFactories.length; i++) {
        try {
            xmlhttp = XMLHttpFactories[i]();
        }
        catch (e) {
            continue;
        }
        break;
    }
    return xmlhttp;
}
