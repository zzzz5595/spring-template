package com.app.domain.member.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberType {

    // Enum 상수 정의
    KAKAO; // 현재 Enum에는 'KAKAO'라는 한 가지 상수만 정의되어 있음

    /**
     * from() 메서드
     * 주어진 문자열을 Enum 상수로 변환하는 메서드입니다.
     * 문자열을 대문자로 변환한 후, 해당 문자열이 Enum의 상수와 일치하는지 확인하고
     * 일치하는 Enum 상수를 반환합니다.
     *
     * @param type 문자열 (예: "kakao")
     * @return 해당 문자열에 대응하는 MemberType 상수 (예: MemberType.KAKAO)
     */
    public static MemberType from(String type) {
        // valueOf(): 주어진 문자열을 Enum 상수로 변환해주는 메서드입니다.
        // 문자열을 대문자로 변환한 후, Enum 상수와 비교합니다.
        // 예시: "kakao"를 입력하면 "KAKAO"로 변환되고, MemberType.KAKAO가 반환됩니다.
        return MemberType.valueOf(type.toUpperCase());
    }

    /**
     * isMemberType() 메서드
     * 주어진 MemberType이 현재 Enum에 정의된 상수인지 확인하는 메서드입니다.
     * Enum.values()를 사용하여 모든 Enum 상수를 배열로 가져온 후, 주어진 타입과 일치하는지 필터링하여 확인합니다.
     *
     * @param type 확인하려는 MemberType 값
     * @return 해당 MemberType이 Enum에 존재하면 true, 그렇지 않으면 false
     */
    public static boolean isMemberType(String type) {
        // MemberType.values(): Enum에 정의된 모든 상수를 배열로 반환합니다.
        // Arrays.stream(): 이 배열을 스트림으로 변환하여 필터링을 수행할 수 있습니다.
        List<MemberType> memberTypes = Arrays.stream(MemberType.values())
                .filter(memberType -> memberType.name().equals(type))
                .collect(Collectors.toList());

        // 필터링 결과가 1개 이상이면 해당 type은 Enum에 존재한다는 의미이므로 true를 반환합니다.
        return memberTypes.size() != 0;
    }
}
