package com.msa.rental.domain.model;

import com.msa.rental.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalCard {
    private RentalCardNo rentalCardNo;
    private IDName member;
    private RentStatus rentStatus;
    private LateFee lateFee;
    private List<RentalItem> rentalItemList = new ArrayList<RentalItem>();
    private List<ReturnItem> returnItemList = new ArrayList<ReturnItem>();

    public static RentalCard sample() {
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(IDName.sample());
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setLateFee(LateFee.sample());
        return rentalCard;
    }

    private void addRentalItem(RentalItem rentalItem) {
        this.rentalItemList.add(rentalItem);
    }

    private void removeRentalItem(RentalItem rentalItem) {
        this.rentalItemList.remove(rentalItem);
    }

    private void addReturnItem(ReturnItem returnItem) {
        this.returnItemList.add(returnItem);
    }

    //대여 카드 생성
    private static RentalCard createRentalCard(IDName creator){
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(creator);
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setLateFee(LateFee.createLateFee());
        return rentalCard;
    }

    //대여 처리
    public RentalCard rentItem(Item item) throws IllegalAccessException {
        checkRentalAvailable();
        this.addRentalItem(RentalItem.createRentalItem(item));
        return this;
    }

    private void checkRentalAvailable() throws IllegalAccessException {
        if (this.rentStatus == RentStatus.RENT_UNAVAILABLE) throw new IllegalAccessException("대여 불가 상태입니다.");
        if (this.rentalItemList.size() > 5) throw new IllegalAccessException("이미 5권을 대여했습니다.");
    }

    public RentalCard returnItem(Item item, LocalDate returnDate){
        RentalItem rentalItem = this.rentalItemList.stream().filter(i -> i.getItem().equals(item)).findFirst().get();
        calcateLateFee(rentalItem, returnDate);
        this.addReturnItem(ReturnItem.createReturnItem(rentalItem));
        this.removeRentalItem(rentalItem);
        return this;
    }

    private void calcateLateFee(RentalItem rentalItem, LocalDate returnDate) {
        if(returnDate.compareTo(rentalItem.getOverdueDate()) > 0){
            int point = Period.between(rentalItem.getOverdueDate(), returnDate).getDays() * 10;
            this.lateFee.addPoint((long) point);
        }
    }

    public RentalCard overdueItem(Item item){
        RentalItem rentalItem = this.rentalItemList.stream().filter(i -> i.getItem().equals(item)).findFirst().get();
        rentalItem.setOverdued(true);
        this.rentStatus = RentStatus.RENT_UNAVAILABLE;
        // 연체 억지로 만들기
        rentalItem.setOverdueDate(LocalDate.now().minusDays(1));
        return this;
    }

    public Long makeAvailableRental(Long point) throws Exception {

        if (this.rentalItemList.size() != 0) throw new IllegalAccessException("모든 도서가 반납되어야 정지를 해제할 수 잇습니다,.");
        if (this.getLateFee().getPoint() != point) throw new IllegalArgumentException("해당 포인트로 연체를 해지할 수 없습니다.");

        this.setLateFee(lateFee.removePoint(point));
        if (this.getLateFee().getPoint() == 0){
            this.rentStatus = RentStatus.RENT_AVAILABLE;
        }
        return this.getLateFee().getPoint();
    }
}
