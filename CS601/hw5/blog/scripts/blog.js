function postBlogEntry()
{
    var textBody = document.getElementById("blogSpace").value;
    
    if (textBody != "")
    {
        var today = new Date();
        var newEntry = document.createElement("fieldset");
        
        newEntry.innerHTML = "<legend>Posted on: " + today.toString() + "</legend>" + textBody;
        document.getElementById("postedBlogs").appendChild(newEntry);
    }
}

function clearForm()
{
    document.getElementById("blogSpace").value = "";
}