import {
	Button,
	Form,
	Grid,
	Input,
	Message,
	Modal,
	Radio,
} from '@arco-design/web-react';
import '@arco-design/web-react/dist/css/arco.css';
import { IconPlusCircle } from '@arco-design/web-react/icon';
import { useRef, useState } from 'react';

const Row = Grid.Row;
const Col = Grid.Col;

const FormItem = Form.Item;
const RadioGroup = Radio.Group;
const App = ({
	visible1,
	setVisible1,
	url,
}: {
	visible1: boolean;
	setVisible1: React.Dispatch<React.SetStateAction<boolean>>;
	url: string;
}) => {
	const [name] = useState('course');
	const [loading1, setLoading1] = useState(false);
	function Forms() {
		const handleUpload = async () => {
			console.log(JSON.stringify(formRef.current.getValues()));
			return false;
		};
		const formRef = useRef<any>();
		return (
			<Modal
				title="Modal Title"
				footer={null}
				visible={visible1}
				onCancel={() => {
					setVisible1(false);
				}}
			>
				<Form ref={formRef} autoComplete="off" layout={'horizontal'}>
					<FormItem label="姓名" field="username" rules={[{ required: true }]}>
						<Input
							style={{ width: 270 }}
							placeholder="please enter your name"
						/>
					</FormItem>
					<FormItem label="密码" field="password" rules={[{ required: true }]}>
						<Input
							style={{ width: 270 }}
							placeholder="please enter your post"
						/>
					</FormItem>
					<FormItem label="年龄" field="age" rules={[{ required: true }]}>
						<Input
							style={{ width: 270 }}
							placeholder="please enter your post"
						/>
					</FormItem>
					<FormItem label="性别" field="sex" rules={[{ required: true }]}>
						<RadioGroup>
							<Radio value="man">男</Radio>
							<Radio value="female">女</Radio>
						</RadioGroup>
					</FormItem>
					<FormItem field="dept" label="院系" rules={[{ required: true }]}>
						<Input
							style={{ width: 270 }}
							placeholder="please enter your post"
						/>
					</FormItem>
					<FormItem>
						<Button
							onClick={async () => {
								if (formRef.current) {
									try {
										await formRef.current.validate();
										console.log(formRef.current.getFieldsValue());
										Message.info('校验通过，提交成功！');
									} catch (_) {
										console.log(formRef.current.getFieldsError());
										Message.error('校验失败，请检查字段！');
									}
								}
							}}
							type="primary"
							style={{ marginRight: 24 }}
						>
							Submit
						</Button>
					</FormItem>
				</Form>
			</Modal>
		);
	}

	return (
		<>
			<Row justify="end">
				<Col span={2}>
					<Button
						type="primary"
						onClick={() => {
							setVisible1(true);
						}}
						icon={<IconPlusCircle />}
					>
						添加
					</Button>
				</Col>
			</Row>

			<Forms />
		</>
	);
};

export default App;
