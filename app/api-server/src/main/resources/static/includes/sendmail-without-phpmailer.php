<?php

if( $_SERVER['REQUEST_METHOD'] == 'POST' ) {
  // Multiple recipients
  $to = 'ismailcseku@gmail.com';


  $name = $_POST['form_name'];
  $email = $_POST['form_email'];
  $subject = $_POST['form_subject'];
  $phone = $_POST['form_phone'];
  $message = $_POST['form_message'];

  $referrer = $_SERVER['HTTP_REFERER'] ? '<br><br><br>This Form was submitted from: ' . $_SERVER['HTTP_REFERER'] : '';


  $name = isset($name) ? "Name: $name<br><br>" : '';
  $email = isset($email) ? "Email: $email<br><br>" : '';
  $phone = isset($phone) ? "Phone: $phone<br><br>" : '';
  $message = isset($message) ? "Message: $message<br><br>" : '';
  $subject = isset($subject) ? $subject : 'New Message | Contact Form';

  // Message
  $message = "$name $email $phone $message $referrer";

  // To send HTML mail, the Content-type header must be set
  $headers[] = 'MIME-Version: 1.0';
  $headers[] = 'Content-type: text/html; charset=iso-8859-1';


  // Mail it
  mail($to, $subject, $message, implode("\r\n", $headers));
}
?>