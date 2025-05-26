function copyToClipBoard(inputId, messageId) {
    const input = document.getElementById(inputId);
    const msg = document.getElementById(messageId);

    if (input && msg) {
        navigator.clipboard.writeText(input.value).then(() => {
            msg.style.display = "block";
            setTimeout(() => msg.style.display = "none", 2000);
        }).catch(err => {
            console.error('Failed to copy: ', err);
        });
    } else {
        console.error('Input or message element not found!');
    }
}