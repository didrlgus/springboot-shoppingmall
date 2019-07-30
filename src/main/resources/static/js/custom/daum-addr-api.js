var registAddrObj = {
    // 도로명주소
    searchAddrFn   : function() {
        var roadAddress = "";
        //load함수를 이용하여 core스크립트의 로딩이 완료된 후, 우편번호 서비스를 실행합니다.
        daum.postcode.load(function(){
            new daum.Postcode({
                oncomplete: function(data) {
                    $("input[name='roadAddr']").val(data.roadAddress);
                    $("input[name='buildingName']").val(data.buildingName);
                }
            }).open();
        });
    }
}