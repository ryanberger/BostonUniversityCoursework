
// Collects radio button value submitted by user and outputs appropriate message to alert box
function fav_color(){
    var f = document.forms[0];
    var choice = f.elements.color_choice;
    for (var i = 0; i < choice.length; i++)
    {
        if (choice[i].checked)
        {
            break;
        }
    }
    if (choice[i].value == 'none')
    {
        alert('I\'m sorry that you don\'t have a favorite color. That\'s very sad!');
    }
    else
    {
        alert('Hey! My favorite color is ' + choice[i].value + ' too!');
    }
}

// Collects all checkbox values selected by user and outputs them to an alert box
function subscribe(){
    var f = document.forms[0];
    var choices = f.elements.magazines;
    var subscriptions = "";
    for (var j = 0; j < choices.length; j++)
    {
        if (choices[j].checked)
        {
            subscriptions += choices[j].value + "\n";
        }
    }
    if (subscriptions.length > 0)
    {
        alert('Congratulations! You will be subscribed to:\n' + subscriptions + '\n(Not really, but it\'s fun to pretend!)');
    }
    else
    {
        alert('You need to pick at least one magazine!');
    }
}