$(document).ready(function(){
        $('#send_message').click(function(e){
            
            //Stop form submission & check the validation
            e.preventDefault();
            
            // Variable declaration
            var error = false;
            var name = $('#name').val();
            var email = $('#email').val();
            var phone = $('#phone').val();
            var message = $('#message').val();
            
            $('#name,#email,#phone,#message').click(function(){
                $(this).removeClass("error_input");
            });
            
            // Form field validation
            if(name.length == 0){
                var error = true;
                $('#name').addClass("error_input");
            }else{
                $('#name').removeClass("error_input");
            }
            if(email.length == 0 || email.indexOf('@') == '-1'){
                var error = true;
                $('#email').addClass("error_input");
            }else{
                $('#email').removeClass("error_input");
            }
            if(phone.length == 0){
                var error = true;
                $('#phone').addClass("error_input");
            }else{
                $('#phone').removeClass("error_input");
            }
            if(message.length == 0){
                var error = true;
                $('#message').addClass("error_input");
            }else{
                $('#message').removeClass("error_input");
            }
            
            // If there is no validation error, next to process the mail function
            if(error == false){
               // Disable submit button just after the form processed 1st time successfully.
                $('#send_message').attr({'disabled' : 'true', 'value' : 'Sending...' });
                
                /* Post Ajax function of jQuery to get all the data from the submission of the form as soon as the form sends the values to contact.php*/
                    $.ajax({

                        url: baseUrl + "/contact",

                        // đúng — lấy context path động

    type: "POST",
    data: $("#contact_form").serialize(), // ✅ Lấy dữ liệu form
    success: function(result) {
        // ✅ Nếu Servlet trả "sent", nghĩa là lưu thành công
        if (result.trim() === "sent") {
            $('#success_message').fadeIn(500);
            $('#error_message').hide();
            $('#contact_form')[0].reset(); // reset form sau khi gửi
        } else {
            $('#error_message').fadeIn(500);
        }
        // Bật lại nút gửi
        $('#send_message').removeAttr('disabled').attr('value', 'Send Message');
    },
    error: function() {
        // Trường hợp lỗi mạng hoặc lỗi server
        $('#error_message').fadeIn(500);
        $('#send_message').removeAttr('disabled').attr('value', 'Send Message');
    }
});

            }
        });    
    });