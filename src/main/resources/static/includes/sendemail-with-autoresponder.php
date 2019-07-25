<?php

require_once('phpmailer/class.phpmailer.php');
require_once('phpmailer/class.smtp.php');

$mail = new PHPMailer();
$autoresponder = new PHPMailer();

//$mail->SMTPDebug = 3;                               // Enable verbose debug output
$mail->isSMTP();                                      // Set mailer to use SMTP
$mail->Host = 'just55.justhost.com';  // Specify main and backup SMTP servers
$mail->SMTPAuth = true;                               // Enable SMTP authentication
$mail->Username = 'themeforest@ismail-hossain.me';                 // SMTP username
$mail->Password = 'AsDf12**';                           // SMTP password
$mail->SMTPSecure = 'ssl';                            // Enable TLS encryption, `ssl` also accepted
$mail->Port = 465;                                    // TCP port to connect to


if( $_SERVER['REQUEST_METHOD'] == 'POST' ) {
    if( $_POST['contact-form-name'] != '' AND $_POST['contact-form-email'] != '' AND $_POST['contact-form-subject'] != '' ) {

        $name = $_POST['contact-form-name'];
        $email = $_POST['contact-form-email'];
        $subject = $_POST['contact-form-subject'];
        $phone = $_POST['contact-form-phone'];
        $message = $_POST['contact-form-message'];


		$subject = isset($subject) ? $subject : 'New Message From Contact Form';

		$botcheck = $_POST['contact-form-botcheck'];

        $toemail = 'spam.thememascot@gmail.com'; // Your Email Address
        $toname = 'ThemeMascot'; // Your Name

		if( $botcheck == '' ) {

			$mail->SetFrom( $email , $name );
			$mail->AddReplyTo( $email , $name );
			$mail->AddAddress( $toemail , $toname );
			$mail->Subject = $subject;

			$autoresponder->SetFrom( $toemail , $toname );
			$autoresponder->AddReplyTo( $toemail , $toname );
			$autoresponder->AddAddress( $email , $name );
			$autoresponder->Subject = 'We\'ve received your Email';

			$ar_body = "Thank you for contacting us. We will reply within 24 hours.<br><br>Regards,<br>Your Company.";

			$name = isset($name) ? "Name: $name<br><br>" : '';
			$email = isset($email) ? "Email: $email<br><br>" : '';
			$phone = isset($phone) ? "Phone: $phone<br><br>" : '';
			$message = isset($message) ? "Message: $message<br><br>" : '';

			$referrer = $_SERVER['HTTP_REFERER'] ? '<br><br><br>This Form was submitted from: ' . $_SERVER['HTTP_REFERER'] : '';

			$body = "$name $email $phone $message $referrer";

			$ar_body = "Thank you for contacting us. We will reply within 24 hours.<br><br>Regards,<br>Your Company.";

			$autoresponder->MsgHTML( $ar_body );
			$mail->MsgHTML( $body );
			$sendEmail = $mail->Send();

			if( $sendEmail == true ):
				$send_arEmail = $autoresponder->Send();
				echo 'We have <strong>successfully</strong> received your Message and will get Back to you as soon as possible.';
			else:
				echo 'Email <strong>could not</strong> be sent due to some Unexpected Error. Please Try Again later.<br /><br /><strong>Reason:</strong><br />' . $mail->ErrorInfo . '';
			endif;
		} else {
			echo 'Bot <strong>Detected</strong>.! Clean yourself Botster.!';
		}
	} else {
		echo 'Please <strong>Fill up</strong> all the Fields and Try Again.';
	}
} else {
	echo 'An <strong>unexpected error</strong> occured. Please Try Again later.';
}

?>