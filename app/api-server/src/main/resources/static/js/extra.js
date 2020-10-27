$(document).ready(function(e) {
    "use strict";

    //mailchimp
    $('#mailchimp-subscription-form-footer').ajaxChimp({
      callback: mailChimpCallBack,
      url: '//thememascot.us9.list-manage.com/subscribe/post?u=a01f440178e35febc8cf4e51f&amp;id=49d6d30e1e'
    });

    function mailChimpCallBack(resp) {
      // Hide any previous response text
      var $mailchimpform = $('#mailchimp-subscription-form-footer'),
          $response = '';
      $mailchimpform.children(".alert").remove();
      if (resp.result === 'success') {
          $response = '<div class="alert alert-success"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + resp.msg + '</div>';
      } else if (resp.result === 'error') {
          $response = '<div class="alert alert-danger"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + resp.msg + '</div>';
      }
      $mailchimpform.prepend($response);
    }

    //Contact Form Validation
    $("#contact_form").validate({
      submitHandler: function(form) {
        var form_btn = $(form).find('button[type="submit"]');
        var form_result_div = '#form-result';
        $(form_result_div).remove();
        form_btn.before('<div id="form-result" class="alert alert-success" role="alert" style="display: none;"></div>');
        var form_btn_old_msg = form_btn.html();
        form_btn.html(form_btn.prop('disabled', true).data("loading-text"));
        $(form).ajaxSubmit({
          dataType:  'json',
          success: function(data) {
            if( data.status === 'true' ) {
              $(form).find('.form-control').val('');
            }
            form_btn.prop('disabled', false).html(form_btn_old_msg);
            $(form_result_div).html(data.message).fadeIn('slow');
            setTimeout(function(){ $(form_result_div).fadeOut('slow') }, 6000);
          }
        });
      }
    });
});



