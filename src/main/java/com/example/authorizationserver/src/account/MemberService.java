package com.example.authorizationserver.src.account;

import com.example.authorizationserver.src.account.dto.MemberDto;
import com.example.authorizationserver.src.account.dto.PostMemberRes;
import com.example.authorizationserver.src.entity.Member;
import com.example.authorizationserver.src.repository.MemberRepository;
import com.example.authorizationserver.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;


    /**
     * 이메일로 찾은 사용자 id(PK) 반환
     */
    public Member getUser(String email){
        Optional <Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()){
            return member.get();
        } else {
            return null;
        }
    }

    public PostMemberRes registerUser(MemberDto memberDto){
        if(getUser(memberDto.getUser_email()) != null){
            System.out.println("이미 가입된 회원입니다.");
            return null;
        }

        Member member = Member.builder()
                .email(memberDto.getUser_email())
                .name(memberDto.getUser_name())
                .role(memberDto.getRole())
                .department(memberDto.getDepartment())
                .university(memberDto.getUniversity())
                .studentNumber(memberDto.getStudent_number())
                .pictureUrl(memberDto.getPicture_url())
                .build();

        Member postMember = memberRepository.save(member);

        Long id = postMember.getId();
        String role = member.getRole();

        String jwtToken = jwtService.createJwt(id, role);
        PostMemberRes postMemberRes = new PostMemberRes(jwtToken, id);
        return postMemberRes;
    }
}
