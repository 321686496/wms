package com.hongan.template;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes= BaseApplication.class)
public class TestTest {
//    @Data
//    static class Book{
//        private int id;
//        private String title;
//        private String subtitle;
//        private String publisher;
//        private String uploaded_at;
//        private int stock;
//    }
//
//    @Data
//    static class UserType{
//        private int id;
//        private String name;
//    }
//
//    @Data
//    static class User{
//        private int id;
//        private String name;
//        private String password;
//        private int sex;
//        private int u_type;
//    }
//
//    @Data
//    static class BorrowRecord{
//        private int id;
//        private int user_id;
//        private int book_id;
//        private String borrowed_at;
//        private String returned_at;
//    }
//
//    @Data
//    static class ContinueRecord{
//        private int id;
//        private int user_id;
//        private int book_id;
//        private String continued_at;
//        private String returned_at;
//    }
//
//    @Data
//    static class ReturnRecord{
//        private int id;
//        private int user_id;
//        private int book_id;
//        private String returned_at;
//    }
//
//    @Data
//    static class ReserveRecord{
//        private int id;
//        private int user_id;
//        private int book_id;
//        private String reserved_at;
//    }
//
//    @Test
//    void testBorrow(){
//        borrow();
//    }
//
//    void borrow(int bookId,int userId){
//        // 查询图书库存是否充足
//        if(bookStock(bookId)){
//            // 图书库存充足，可借
//            // 判断该用户是否已借过该图书
//            if(!borrowed(bookId,userId)){
//                // 已借，提示用户是否续借
//                if(isContinue("是否续借？")){
//                    // 续借
//                    continueBorrow(bookId,userId);
//                    return;
//                }
//                // 不续借
//                return;
//            }
//            // 未借，开始借书
//            borrowBook(bookId,userId);
//            return;
//        }
//        // 图书库存不足，不可借，查询预约情况
//        if(reserve(bookId,userId)){
//            // 已预约，提示用户等待
//            prompt("请耐心等待");
//            return;
//        }
//        // 未预约，提示用户是否预约
//        if(isReserve(bookId,userId)){
//            // 预约
//            reserve(bookId,userId);
//            return;
//        }
//        // 不预约
//        return;
//    }
}




/*
* drop database test_library_db;
create database test_library_db;

use test_library_db;

create table book(
    id int primary key auto_increment,
    title varchar(255) not null,
    subtitle varchar(255),
    publisher varchar(255),
    uploaded_at datetime default current_timestamp,
    stock int default 0
);

create table user_type(
    id tinyint primary key auto_increment,
    name varchar(255) not null
);

create table user(
    id int primary key auto_increment,
    name varchar(255) not null,
    password varchar(255) not null,
    sex tinyint default 0,
    u_type tinyint default 0,
    constraint fk_user_type foreign key (u_type) references user_type(id)
);

create table borrow_record(
    id int primary key auto_increment,
    user_id int not null,
    book_id int not null,
    borrowed_at datetime default current_timestamp,
    returned_at datetime,
    constraint fk_user foreign key (user_id) references user(id),
    constraint fk_book foreign key (book_id) references book(id)
);

create table continue_record(
    id int primary key auto_increment,
    user_id int not null,
    book_id int not null,
    continued_at datetime default current_timestamp,
    returned_at datetime default current_timestamp,
    constraint fk_user_c foreign key (user_id) references user(id),
    constraint fk_book_c foreign key (book_id) references book(id)
);

create table return_record(
    id int primary key auto_increment,
    user_id int not null,
    book_id int not null,
    returned_at datetime default current_timestamp,
    constraint fk_user_r foreign key (user_id) references user(id)
);

create table reserve_record(
    id int primary key auto_increment,
    user_id int not null,
    book_id int not null,
    reserved_at datetime default current_timestamp,
    constraint fk_user_rs foreign key (user_id) references user(id)
)

* */
