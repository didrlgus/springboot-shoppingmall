package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageImpl;

@Getter
@Setter
@ToString
public class PagingDto {

    private int page            = 1;   // 현재 페이지
    private int scale           = 5;
    private int scaleStartPage  = 1;   // scale 시작 페이지
    private int scaleEndPage    = 1;   // scale 끝 페이지
    private int totPage         = 1;   // 전체 페이지 수
    // private int totCnt       = 0;   // 전체 게시글 수
    private int prevPage        = 0;   // 이전 페이지 ('<<' 버튼 클릭 시)
    private int nextPage        = 0;   // 다음 페이지 ('>>' 버튼 클릭 시)
    /*private String keyword	   = "";
    private Map<String, Object> keywordList;*/

    public void setPagingInfo(PageImpl<?> list) {
        this.page   = list.getNumber() + 1;
        int totPage	= list.getTotalPages();

        int nowScale  = (page - 1) / this.scale + 1;               	// 현재 화면의 그룹

        int startPage = (nowScale - 1) * scale + 1;            	    // 스케일 시작 페이지
        int endPage   = startPage + scale - 1;               		// 스케일 끝 페이지

        endPage       = Math.min(endPage, totPage);

        int prevPage  = startPage - 1;                      		// 이전 페이지
        int nextPage  = endPage + 1;                        		// 다음 페이지

        this.scaleStartPage = startPage;
        this.scaleEndPage   = endPage;
        this.prevPage       = prevPage;
        this.nextPage       = nextPage;
        this.totPage        = totPage;
    }
}
