package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Constant.SocialLoginType;
import com.example.demo.src.account.dto.GetSocialOAuthRes;
import com.example.demo.src.account.dto.MemberDto;
import com.example.demo.src.account.dto.PostMemberRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;
import static com.example.demo.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;

@RestController
@RequestMapping(value = "/accounts")
@AllArgsConstructor
public class AccountController {
    private final OAuthService oauthService;
    private final MemberService memberService;


    @GetMapping("/oauth2/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String socialLoginPath) throws IOException{
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        oauthService.redirectTo(socialLoginType);
    }

    @GetMapping("/oauth2/{socialLoginType}/callback")
    public BaseResponse<GetSocialOAuthRes> socialLogin (@PathVariable(name = "socialLoginType") String socialLoginPath, @RequestParam(name = "code") String code) {
        try{
            SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
            GetSocialOAuthRes getSocialOAuthRes = oauthService.oAuthLogin(socialLoginType, code);
            return new BaseResponse<>(getSocialOAuthRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/signup")
    public BaseResponse<PostMemberRes> signUp (HttpServletRequest request, @RequestBody MemberDto memberDto) {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String pictureUrl = (String) session.getAttribute("pictureUrl");

        if (email != null) {
            memberDto.setUser_email(email);
            memberDto.setPicture_url(pictureUrl);
            PostMemberRes postMemberRes = memberService.registerUser(memberDto);
            if (postMemberRes != null) return new BaseResponse<>(postMemberRes);
            else return new BaseResponse<>(POST_USERS_EXISTS_EMAIL);
        } else {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
    }

}
