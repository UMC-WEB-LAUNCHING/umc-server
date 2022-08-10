import React, { useState, useRef } from 'react'
import Axios from 'axios';
import { useDispatch } from 'react-redux';
import {loginUser} from '../../../_actions/user_action'
import { useNavigate } from 'react-router-dom';
import style from './LoginStyle.module.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faLock, faEye } from "@fortawesome/free-solid-svg-icons";
import { Link } from 'react-router-dom'

function LoginPage(props) {
  const Dispatch = useDispatch();
  let navigate = useNavigate();

  let errorMsgRef = useRef();

  const [Email, setEmail] = useState("");
  const [Password, setPassword] = useState("");

  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value)
  }
  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value)
  }

  const onSubmitHandler = (event) => {
    event.preventDefault();

    let body = {
      email : Email,
      password : Password 
    }

    Dispatch(loginUser(body))
    .then(response =>{
      if(response.payload.loginSuccess) {
        navigate('/')
      } else {
        errorMsgRef.current.style = "visibility: visible"
        // alert('error occur')
        // alert('error on')
        // console.log(errorMsgRef);
        // errorMsgRef[0].style.display = 'block';
      }
    })

    
  }
// 폰트랑 Create~ 수정해야함
  return (
    <div id={style.loginPage}>
        <div className={style.leftBlue}>
        <img src="LoginImages/LogoImage.png" id={style.logoImage} alt="LogoImage" />
        </div>

        <div className={style.rightWhite}>
            <div id={style.createAccount}>
                <Link to="/register" style = {{fontSize : '18px', fontWeight : '800', color:'#267dff'}}>Create an account</Link>
            </div>
            <div id={style.rightContents}>
                <div id={style.wrap}>

                    <div id={style.loginTitle}>
                        <div id={style.welcome}>
                            <h2>Welcome to (과제헬퍼)!</h2>
                        </div>
                        <p>Register your account</p>
                    </div>

                    <div id={style.login}>
                        <form onSubmit={onSubmitHandler}>
                            <div id={style.email}>
                                <input type="email" name="email" placeholder="이메일 (로그인 시 필요)" id={style.emailInput} value={Email} onChange={onEmailHandler}/>
                                <FontAwesomeIcon icon={faEnvelope} id={style.envelopeIcon} />
                                
                            </div>
                            <div id={style.password}>
                                <input type="password" name="password" placeholder="비밀번호" id={style.passwordInput} value={Password} onChange={onPasswordHandler} />
                                <FontAwesomeIcon icon={faLock} id={style.lockIcon} />
                                <FontAwesomeIcon icon={faEye} id={style.eyeIcon} />
                                
                            </div>


                            <div ref={errorMsgRef} className={style.loginError}>아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.</div>

                            
                            <div id={style.buttonLogin}>
                                <input type="submit" value="로그인" id={style.loginBtn} />
                            </div>
                        </form>

                        <div className={style.loginTrouble}>
                            <span id={style.forget1}>Forgot your </span>
                            <a href="frontend/client/src/components/views/LoginPage/LoginPage" id={style.forget2}>Username </a>
                            <span id={style.forget3}>and </span>
                            <a href="frontend/client/src/components/views/LoginPage/LoginPage" id={style.forget4}>Password?</a>
                        </div>
                    </div>

                    <div id={style.OR}>
                        <span className={style.before}></span>
                        <p>OR</p>
                        <span className={style.after}></span>
                    </div>
                    
                    <p id={style.Easier}>Easier sign in</p>

                    <div id={style.socialLogin}>
                        <a href="www.naver.com">
                            <span className={style.logoBnt}>
                                <img src="LoginImages/button_네이버.png" width='65px' height='65px' alt="네이버" />
                            </span>
                        </a>
                        <a href="www.naver.com">
                            <span className={style.logoBnt}>
                                <img src="LoginImages/button_구글.png" width='65px' height='65px' alt="구글" />
                            </span>
                        </a>
                
                        <a href="www.naver.com">
                            <span className={style.logoBnt}>
                                <img src="LoginImages/button_페이스북.png" width='65px' height='65px' alt="페이스북" />
                            </span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
  )
}
//   return (
//     <div style={{
//       display:'flex', justifyContent : 'center', alignItems : 'center',
//       width : '100%', height :'100%'
//     }}>
//       <form style = {{ display : 'flex', flexDirection : 'column' }}
//       onSubmit={onSubmitHandler}>
//         <label>Email</label>
//         <input type="email" value={Email} onChange={onEmailHandler}/>
//         <label>password</label>
//         <input type="password" value={Password} onChange={onPasswordHandler}/>
//         <br />
//         <button type="submit">Login</button>
//       </form>
//       </div>
//   )
// }

export default LoginPage;