import { Link,useNavigate } from "react-router-dom";
import NewNav from "../components/layout/NewNav.jsx";
import Styles from "./User.module.css";
import { useState } from "react";
import axios from "axios";

import * as React from 'react';
import UserSearch2 from "../components/admin/Usesearch2.jsx";
import Userqnalist from "../components/admin/Userqnalist.jsx";
import Swal from "sweetalert2";
import { web3 } from "../contracts/CallContract";

function User() {

    const token = sessionStorage.getItem("token")


    const [clickCom, setClickCom] = useState('#FEFFF8');
    
    function changeColor() {
        setClickCom('#caceb7');
    }

    //기업 회원가입 띄우기_기본값은 true로 hidden
    const [open, setOpen] = useState(true);
    const getOpen = () => {
        //기업관리 값 변경시
        setOpen(!open);
        //회원관리가 켜져있다면
        if(openO === false){
            //hidden처리 하기.
            setOpenO(true);
        }
        if(openL === false){
            //hidden처리 하기.
            setOpenO(true);
        }
    }

    //회원 관리 띄우기
    const [openO, setOpenO] = useState(true);
    const getOpenO = () => {
        setOpenO(!openO);
        if(open === false){
            setOpen(true);
        }
        if(openL === false){
            setOpenL(true);
        }
    }

    //회원 문의 리스트
    const [openL, setOpenL] = useState(true);
    const getOpenL = () => {
        setOpenL(!openL);
        if(open === false){
            setOpen(true);
        }
        if(openO === false){
            setOpenO(true);
        }
    }

    // 담당자 이메일 받아오기
    const [email, setEmail] = useState("");
    const getEmail = (e) => {
        setEmail(e.target.value);
        console.log(email);
    };

          //닉네임 사용 가능
          const usenick = () => {
            Swal.fire({
              text:"사용가능한 닉네임입니다.",
              icon: 'success',
              confirmButtonColor: '#73E0C1',
              confirmButtonText: '확인'
            })
        }
    
        //닉네임 중복
        const samenick = () => {
            Swal.fire({
              text:"동일 닉네임이 존재합니다.",
              icon: 'error',
              confirmButtonColor: '#73E0C1',
              confirmButtonText: '확인'
            })
        }
    
        //닉네임 빈값
        const nicknull = () => {
            Swal.fire({
              text:"Nickname을 입력해주세요.",
              icon: 'error',
              confirmButtonColor: '#73E0C1',
              confirmButtonText: '확인'
            })
        }

    //nickname -> 회사명 받아오기
    const [nickname, setId] = useState("");
    const getId = (e) => {
        setId(e.target.value);
        console.log(nickname);
    };

    const [checknick, setChecknick] = useState(false);

    const getChecknick = (e) => {
        if (nickname === "") {
            nicknull();
        } else {
            axios
              .get(
                `https://j6a304.p.ssafy.io/api/members/nickname/${nickname}`,
                {
                    n: nickname,
                }
              )
              .then((res) => {
                usenick();
                setChecknick(true);
              })
              .catch((error) => {
                console.log("error", error.response);
                if (error.code === 409) {
                    samenick();
                }
                setId("");
              });
        }
    };

    //비밀번호 받아오기
    const [password, setPassword] = useState("");
    const getPassword = (e) => {
        setPassword(e.target.value);
        console.log(password);
    };

    //담당자 번호
    const [phone, setPhone] = useState("");
    const getPhone = (e) => {
        setPhone(e.target.value);
        console.log(phone);
    };

    //계좌 비밀번호
    const [walletpw, setWalletpw] = useState("");
    const [userAccount, setUserAccount] = useState("");
    const getWalletpw = (e) => {
      setWalletpw(e.target.value);
    };
    const createWallet = async () => {
      let userAccount = await web3.eth.personal.newAccount(walletpw);
      return userAccount;
      // setState는 비동기처리이기 때문에 바로 console에 변한 값이 출력되지 않음
    };

    //alert 창_회원가입 
    const joinSuccess = () => {
        Swal.fire({
          title: "회원가입 성공!!",
          text: "POLLING에 오신 것을 환영합니다!",
          icon: "success",
          confirmButtonColor: "#73E0C1",
          confirmButtonText: "확인",
        })
    };
    
    const joinFail = () => {
        Swal.fire({
          title:"회원가입 실패!",
          icon: 'error',
          confirmButtonColor: '#73E0C1',
          confirmButtonText: '확인'
        })
    }

    //빈칸확인
    const inputnull = () => {
        Swal.fire({
          text:"닉네임/이메일/비밀번호/휴대폰번호/계좌 비밀번호를 입력하세요.",
          icon: 'error',
          confirmButtonColor: '#73E0C1',
          confirmButtonText: '확인'
        })
    }

    //페이지 이동
    const navigate = useNavigate();

    // 회원가입하기
    const onLogin = async (e) => {
        if(nickname ===" " || email === " " || password === " " || phone === " " || walletpw === " "){
            inputnull();
            e.preventDefault();
        } 
        else if(nickname !== " " && email !== " " && password !== " " && walletpw !== " " ){
            const wallet = await createWallet();

            axios
            .post(
                "http://j6a304.p.ssafy.io/api/members",
                {
                    email: email,
                    nickname: nickname,
                    password: password,
                    phoneNumber: phone,
                    wallet: wallet,
                    role: "ROLE_COMPANY"
                },
            )
            .then((res) => {
                console.log("res", res);
                joinSuccess();
            })
            .catch(error => {
                const message = error.message;
                console.log("message", message);
                joinFail();
              });
        }
    };



    return (
        <div style={{height:'100vh'}}>
            <NewNav />
            <div className={Styles.user}>User Mgt</div>
                <div className={Styles.nav}>
                    <div className={Styles.addcompany} onClick={getOpen}>
                        <summary> 기업 회원 가입 </summary>
                    </div>
                    <div className={Styles.other} onClick={getOpenO}>
                    <summary>회원 관리</summary>
                    </div>
                    <div className={Styles.qna} onClick={getOpenL}>
                    <summary>1:1 문의</summary>
                    </div>
                </div>

                <div hidden={open}>
                    <div className={Styles.login}>
                        <div> 
                            <input type={"text"} placeholder=" Business_name " className={Styles.id} onChange={getId} name="nickname" maxLength="12"/>
                            <button className={Styles.nicknameCheck} onClick={getChecknick} disabled={checknick === true}>
                                중복확인
                            </button>
                            <input type={"email"} placeholder=" email" className={Styles.email} onChange={getEmail} name="email"/>
                            <input type={"password"} placeholder=" Password" className={Styles.password} onChange={getPassword} name="password" maxLength="13"/>
                            <input type={"text"} placeholder=" PhoneNumber(01012345678) " className={Styles.phone} onChange={getPhone} name="phone"/>
                            <input type={"password"} placeholder=" Wallet Password " className={Styles.walletpassword} onChange={getPhone} name="phone"/>
                            <button className={Styles.signinbtn} onClick={onLogin}>Create</button>
                        </div>
                    </div>
                </div>

                <div hidden={openO}>
                    {/* 회원리스트 주루륵 */}

                    <UserSearch2 />
                </div>
                
                <div hidden={openL}>
                    {/* 1:1문의 내용 주르륵 */}
                    <Userqnalist />
                </div>
        </div>
    );
}

export default User;