package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.MeRequestDto;
import com.shoppingmall.dto.UpdatePasswordRequestDto;
import com.shoppingmall.service.NormalUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Api(tags = "user", description = "회원")
@RestController
public class UserRestController {

    private NormalUserService normalUserService;

    @ApiOperation(value = "회원 중복 확인")
    @GetMapping("/duplicateCheck")
    public String duplicateCheck(@RequestParam Map<String, Object> identifier) {

        return normalUserService.duplicateCheck(identifier);
    }

    @ApiOperation(value = "회원 상세")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/me/{id}")
    public ResponseEntity<?> getProfiles(@PathVariable Long id) {

        return ResponseEntity.ok().body(normalUserService.getProfiles(id));
    }

    @ApiOperation(value = "회원 프로필 수정")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/me/{id}")
    public ResponseEntity<String> modifyProfiles(HttpServletRequest request, @PathVariable Long id,
                                                 @RequestBody @Valid MeRequestDto meRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        request.getSession().setAttribute("user", normalUserService.updateProfiles(id, meRequestDto));

        return ResponseEntity.ok("프로필 수정이 완료되었습니다!");
    }

    @ApiOperation(value = "회원 삭제")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/me/{id}")
    public ResponseEntity<String> deleteProfiles(@PathVariable Long id) {

        normalUserService.deleteProfiles(id);

        return ResponseEntity.ok().body("탈퇴가 완료되었습니다.");
    }

    @ApiOperation(value = "회원 비밀번호 수정")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/me/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id,
                                                 @RequestBody @Valid UpdatePasswordRequestDto passwordRequestDto,
                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        normalUserService.updatePassword(id, passwordRequestDto);

        return ResponseEntity.ok().body("비밀번호 수정이 완료되었습니다.");
    }
}
