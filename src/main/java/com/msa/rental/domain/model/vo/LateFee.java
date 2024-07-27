package com.msa.rental.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LateFee {
    private Long point;

    public LateFee addPoint(Long point) {
        return new LateFee(this.point + point);
    }

    public LateFee removePoint(Long point) throws Exception {
        if (point > this.point) {throw new Exception("보유한 포인트보다 커서 삭제할 수 업습니다.");}

        return new LateFee(this.point - point);
    }

    public static LateFee createLateFee() {
        return new LateFee(0L);
    }

    public static LateFee sample(){
        return new LateFee(100L);
    }
}
