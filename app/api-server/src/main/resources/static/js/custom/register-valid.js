// 유효성 콜백
var validCallback = {

    // 아이디 중복 체크 후 변경 될 가능성이 존재하니 임시로 저장하는 값
    tempId		: "",
    idDupChk	: false,

    // 아이디 중복 체크
    duplicateChkFnCb : function() {
        var that = this;

        var identifier = $("#register-identifier").val();

        if (!Regex.identifier.test(identifier)) {
            alert("사용 불가한 아이디 입니다!");

            $("#register-identifier").focus();

            return false;
        }

        $.ajax({
            url : "/duplicateCheck?identifier=" + identifier,
            type: "get",
            contentType : "application/json",
            async : false,
            success : function (data) {
                if (data) {
                    alert("사용 가능한 아이디 입니다!");
                    that.idDupChk = true;
                    that.tempId   = identifier;
                }
            },
            error : function (e) {
                alert(e.responseText);
            }
        })
    },

    // 빈값 체크
    emptyChkFnCb : function() {
        var valid = false;

        $("input.registrationInput").each(function() {
            var $this = $(this),
                removeBlankData = $this.val().replace(Regex.blank, ""),     // 빈칸 (스페이스바) 입력 시 공백지움
                dataName = $this.data("name");

            $this.val(removeBlankData);

            // 공백이 아닐 경우 if
            if ($this.val() !== "") {
                valid = true;
            } else {
                var text = dataName;

                alert(text + "은/는 필수 입력 값입니다.");

                $this.focus();
                valid = false;

                // each문 종료
                return false;
            }
        })
        return valid;
    },

    // 이메일 유효성 체크
    emailValidChkFnCb : function(email) {
        if (!Regex.email.test(email)) {
            alert("유효하지 형식의 이메일 입니다!");

            $("#register-email").focus();

            return false;
        }

        return true;
    },

    pwdValidChkFnCb   : function(password) {
        if (!Regex.password.test(password)) {
            alert("유효하지 형식의 비밀번호 입니다!");

            $("#register-password").focus();

            return false;
        }

        return true;
    },

    pwdConfirmValidChkFnCb    : function(password, pwdConfirm) {
        if (password !== pwdConfirm) {
            alert("첫번째 입력한 비밀번호와 같지 않습니다!");

            $("#register-passwordConfirm").focus();

            return false;
        }

        return true;
    },

    // 입력 데이터 유효성 체크
    registerValidChkFnCb : function() {
        var identifier = $("#register-identifier").val(),
            password = $("#register-password").val();

        if (this.tempId !== identifier) {

            alert("아이디 중복 체크 해주세요!")

            $("#dupChkBtn").focus();

            return false;
        }

        if (this.emptyChkFn() && this.emailValidChkFn($("#register-email").val())
            && this.pwdValidChkFn(password) && this.pwdConfirmValidChkFn(password, $("#register-passwordConfirm").val())) {

            return true;
        } else
            return false;
    }
}
