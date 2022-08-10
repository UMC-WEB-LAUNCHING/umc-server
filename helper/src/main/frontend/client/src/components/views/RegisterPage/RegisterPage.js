import React, { useState, useRef } from 'react';
import { useDispatch } from 'react-redux';
import {registerUser} from '../../../_actions/user_action'
import { useNavigate } from 'react-router-dom';
import style from './RegisterStyle.module.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faEnvelope, faLock, faEyeSlash } from "@fortawesome/free-solid-svg-icons";

function RegisterPage(props) {

  const Dispatch = useDispatch();
  let navigate = useNavigate();

  let mustCheckBox = useRef();
  let noCheckMsg = useRef();
  let PasswordLength = useRef();

  const [Email, setEmail] = useState("");
  const [Name, setName] = useState("");
  const [Password, setPassword] = useState("");
  const [ConfirmPassword, setConfirmPassword] = useState("");


  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value)
  }
  const onNameHandler = (event) => {
    setName(event.currentTarget.value)
  }
  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value)
  }
  const onConfirmPasswordHandler = (event) => {
    setConfirmPassword(event.currentTarget.value)
  }

  const onSubmitHandler = (event) => {
    event.preventDefault();
    if(Password.length<6) {
      PasswordLength.current.style = "visibility: visible"
      return
    }
    if(Password != ConfirmPassword) {
      // console.log(mustCheckBox.current.checked);
      return alert('비밀번호와 비밀번호 확인은 같아야합니다.')
    }
    if(!mustCheckBox.current.checked) {
      noCheckMsg.current.style = "visibility: visible"
      return
    }
    let body = {
      email : Email,
      name : Name,
      password : Password
    }
    
    Dispatch(registerUser(body))
    .then(response =>{
      if(response.payload.registerSuccess) {
        navigate('/login')
      } else {
        alert('Register error')
        
      }
    })

}

  return (
    <div id={style.registerPage}>
        <div class={style.leftBlue}>
        <img src="LoginImages/LogoImage.png" id={style.logoImage} alt="LogoImage" />
        </div>

        <div class={style.rightWhite}>
            <div id={style.rightContents}>
                <div id={style.wrap}>


                    <div id={style.registerTitle}>
                        <div id={style.welcome}>
                            <h2>Welcome to (과제헬퍼)!</h2>
                        </div>
                        <p>Register your account</p>
                    </div>

                
                    <div id={style.register}>

                        <form onSubmit={onSubmitHandler}>
                            <div id={style.name}>
                                <input type="text" name="name" placeholder="이름" id={style.nameInput} value={Name} onChange={onNameHandler} />
                                <FontAwesomeIcon icon={faUser} id={style.userIcon} />
                            </div>

                            <div id={style.email}>
                                <input type="email" name="email" placeholder="이메일 (로그인 시 필요)" id={style.emailInput} value={Email} onChange={onEmailHandler} />
                                <FontAwesomeIcon icon={faEnvelope} id={style.envelopeIcon} />
                            </div>
            
                            <div id={style.password}>
                                <input type="password" name="password" placeholder="비밀번호" id={style.passwordInput} value={Password} onChange={onPasswordHandler} />
                                <FontAwesomeIcon icon={faLock} className={style.lockIcon} />
                                <FontAwesomeIcon icon={faEyeSlash} className={style.eyeSlashIcon} />
                            </div>
                            <div ref={PasswordLength} className={style.passwordLength}>6글자 이상이어야 합니다.</div>
                          {/* 임시 비밀번호 확인 */}
                            <div id={style.confirmPassword}>
                                <input type="password" name="confirm_password" placeholder="비밀번호확인" id={style.confirmPasswordInput} value={ConfirmPassword} onChange={onConfirmPasswordHandler} />
                                <FontAwesomeIcon icon={faLock} className={style.lockIcon} />
                                <FontAwesomeIcon icon={faEyeSlash} className={style.eyeSlashIcon} />
                            </div>

                            <div class={style.pwCondition}>
                                <span>영문, 숫자, 특수문자 2개 조합 10자 이상</span>
                            </div>

                            <div id={style.agreement}>
                                <div class={style.btn} id={style.btn1}>
                                    <input type="checkbox" id={style.check1} ref= {mustCheckBox}/>
                                    <span class={style.checkSentence}>(필수) 서비스 이용약관, 개인정보 처리방침에 동의</span>
                                </div>
                                <div class={style.btn} id={style.btn2}>
                                    <input type="checkbox" id={style.check2} />
                                    <span class={style.checkSentence}>(선택) 홍보 및 이벤트/ 마케팅 이용동의</span>
                                </div>
                            </div>

                            <div id={style.signIn}>
                              <div ref={noCheckMsg} className={style.notChecked}>필수 항목 동의가 필요합니다.</div>
                                <input type="submit" value="가입하기" id={style.signInBtn}/>                               
                            </div>
                         </form>
                      </div>

                </div>
          </div>
      </div>
    </div>
  )
}


    // <div style={{
    //   display:'flex', justifyContent : 'center', alignItems : 'center',
    //   width : '100%', height :'100%'
    // }}>
    //   <form style = {{ display : 'flex', flexDirection : 'column' }}
    //   onSubmit={onSubmitHandler}>

    //     <label>Email</label>
    //     <input type="email" value={Email} onChange={onEmailHandler}/>

    //     <label>Name</label>
    //     <input type="text" value={Name} onChange={onNameHandler}/>

    //     <label>Password</label>
    //     <input type="password" value={Password} onChange={onPasswordHandler}/>

    //     <label>Confirm Password</label>
    //     <input type="password" value={ConfirmPassword} onChange={onConfirmPasswordHandler}/>

    //     <br />
    //     <button type="submit">Register</button>
        
    //   </form>
    // </div>

export default RegisterPage;