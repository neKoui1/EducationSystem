import { Form, Radio } from '@arco-design/web-react';
import Router from 'next/router';
import { useState } from 'react';
import style from '../styles/Login.module.css';
const RadioGroup = Radio.Group;
const FormItem = Form.Item;
const SignIn = () => {
	const [id, setEmail] = useState('');
	const [user, setUser] = useState('student');
	const [password, setPassword] = useState('');
	const handleSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.preventDefault();
		fetch(`/api/${user}/login`, {
			method: 'POST',
			body: JSON.stringify({ id, password }),
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then((res) => res.json())
			.then((data) => {
				if (data.code != 200) {
					alert(data.msg);
				} else {
					localStorage.removeItem('student');
					localStorage.removeItem('teacher');
					localStorage.setItem(user, JSON.stringify(data.data));
					if (user != 'admin') Router.push(`/${user}/courses`);
					else {
						Router.push(`/${user}/course`);
					}
				}
			});
	};
	return (
		<div className={style['form-container'] + ' ' + style['sign-in-container']}>
			<form action="#">
				<h1>Sign in</h1>
				<div className={style['social-container']}></div>
				<RadioGroup
					onChange={(value) => {
						setUser(value);
					}}
					type="button"
					name="user"
					defaultValue="student"
					style={{ marginRight: 20, marginBottom: 20 }}
				>
					<Radio value="student">student</Radio>
					<Radio value="teacher">teacher</Radio>
					<Radio value="admin">admin</Radio>
				</RadioGroup>
				<input
					type="text"
					value={id}
					onChange={(e) => {
						setEmail(e.target.value);
					}}
					placeholder="Id"
				/>
				<input
					type="password"
					value={password}
					onChange={(e) => {
						setPassword(e.target.value);
					}}
					placeholder="Password"
				/>
				<a href="#">Forgot your password?</a>
				<button onClick={handleSubmit}>Sign In</button>
			</form>
		</div>
	);
};
const SignUp = () => {
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const handleSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.preventDefault();
	};
	return (
		<div className={style['form-container'] + ' ' + style['sign-up-container']}>
			<form action="#">
				<h1>Sign Up</h1>
				<div className={style['social-container']}></div>
				<span>or use your email for registration</span>
				<input
					type="email"
					value={email}
					onChange={(e) => {
						setEmail(e.target.value);
					}}
					placeholder="Email"
				/>
				<input
					type="password"
					value={password}
					onChange={(e) => {
						setPassword(e.target.value);
					}}
					placeholder="Password"
				/>
				<button onClick={handleSubmit}>Sign Up</button>
			</form>
		</div>
	);
};
interface Props {
	isSignIn?: boolean;
	setIsSignUp: React.Dispatch<React.SetStateAction<boolean>>;
}
const OverlayContainer = ({ setIsSignUp }: Props) => {
	return (
		<div className={style['overlay-container']}>
			<div className={style['overlay']}>
				<div className={style['overlay-panel'] + ' ' + style['overlay-left']}>
					<h1>Welcome Back!</h1>
					<p>To keep connected with us please login with your personal info</p>
					<button
						className={style['ghost']}
						onClick={() => {
							setIsSignUp(false);
						}}
						id="signIn"
					>
						Sign In
					</button>
				</div>
				<div className={style['overlay-panel'] + ' ' + style['overlay-right']}>
					<h1>Hello, Friend!</h1>
					<p>Enter your personal details and start journey with us</p>
				</div>
			</div>
		</div>
	);
};
export default function Login() {
	const [isSignUp, setIsSignUp] = useState(false);
	let styleClass = isSignUp
		? ' ' + style['right-panel-active']
		: ' ' + style['left-panel-active'];
	return (
		<div className={style.body}>
			<h2>选课系统</h2>
			<div className={style['container'] + styleClass}>
				<SignIn />
				<SignUp />
				<OverlayContainer isSignIn={isSignUp} setIsSignUp={setIsSignUp} />
			</div>
		</div>
	);
}
