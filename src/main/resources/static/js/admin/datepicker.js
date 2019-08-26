/*!
 * Datepicker v1.0.0-beta
 * https://fengyuanchen.github.io/datepicker
 * https://fengyuanchen.github.io/datepicker/ 참고
 */
$(function () {

    // 달력
    $('.cal-wrap').each(function () {
        var _btn = $(this).find('.btn-cal');
        $(this).find('.ui-ipt').datepicker({
            language: 'ko-KR',
            trigger: _btn,
            format: 'yyyy-mm-dd',
            autoHide: true
        });
    });

    var $startDt = $("input[name='dpStrtDt']"),
        $endDt   = $("input[name='dpEndDt']");

    $startDt.on("pick.datepicker", function() {
        var date = $startDt.datepicker("getDate");
        var tomorrow = date.setDate(date.getDate() + 1);

        $endDt.datepicker("setStartDate", new Date(tomorrow));
        $endDt.datepicker("setDate", new Date(tomorrow));
    })

})