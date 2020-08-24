package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.MeRequestDto;
import com.shoppingmall.dto.UpdatePasswordRequestDto;
import com.shoppingmall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Api(tags = "user", description = "회원")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    @ApiOperation(value = "회원 중복 확인")
    @GetMapping("/duplicateCheck")
    public ResponseEntity<?> duplicateCheck(@RequestParam String identifier) {

        return ResponseEntity.ok(userService.duplicateCheck(identifier));
    }

    @ApiOperation(value = "회원 상세")
    @GetMapping("/me/{id}")
    public ResponseEntity<?> getProfiles(@PathVariable UUID id) {

        return ResponseEntity.ok().body(userService.getProfiles(id));
    }

    @ApiOperation(value = "회원 프로필 수정")
    @PutMapping("/me/{id}")
    public ResponseEntity<String> modifyProfiles(HttpServletRequest request, @PathVariable UUID id,
                                                 @RequestBody @Valid MeRequestDto meRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        request.getSession().setAttribute("user", userService.updateProfiles(id, meRequestDto));

        return ResponseEntity.ok("프로필 수정이 완료되었습니다!");
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/me/{id}")
    public ResponseEntity<String> deleteProfiles(@PathVariable UUID id) {

        userService.deleteProfiles(id);

        return ResponseEntity.ok().body("탈퇴가 완료되었습니다.");
    }

    @ApiOperation(value = "회원 비밀번호 수정")
    @PutMapping("/me/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable UUID id,
                                                 @RequestBody @Valid UpdatePasswordRequestDto passwordRequestDto,
                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        userService.updatePassword(id, passwordRequestDto);

        return ResponseEntity.ok().body("비밀번호 수정이 완료되었습니다.");
    }
}
