import { Breadcrumb, Grid, Layout, Menu } from '@arco-design/web-react';
import { IconCalendar, IconHome } from '@arco-design/web-react/icon';
import Link from 'next/link';
import Router from 'next/router';
const Row = Grid.Row;
const Col = Grid.Col;

const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;
const Sider = Layout.Sider;
const Header = Layout.Header;
const Footer = Layout.Footer;
const Content = Layout.Content;
const Student = ({ children }: { children: React.ReactNode }) => {
	return (
		<>
			<Layout className="layout-collapse-demo">
				<Sider
					collapsible
					trigger={null}
					breakpoint="xl"
					style={{ height: '100vh' }}
				>
					<div className="logo" />
					<Menu
						defaultOpenKeys={['1']}
						defaultSelectedKeys={['0_3']}
						style={{ width: '100%', height: '100%' }}
					>
						<Link href={'./courses'}>
							<MenuItem key="0_1">
								<IconHome />
								课程查询
							</MenuItem>
						</Link>
						<Link href={'./chosen'}>
							<MenuItem key="0_2">
								<IconCalendar />
								已选课程
							</MenuItem>
						</Link>
						<Link href={'./course'}>
							<MenuItem key="0_2">
								<IconCalendar />
								课程表
							</MenuItem>
						</Link>
						<Link href={'./info'}>
							<MenuItem key="0_2">
								<IconCalendar />
								个人信息
							</MenuItem>
						</Link>
						<MenuItem
							key="0_4"
							style={{ marginTop: 'calc(100vh - 240px)' }}
							onClick={() => {
								Router.push('/');
							}}
						>
							<IconCalendar />
							退出登录
						</MenuItem>
					</Menu>
				</Sider>
				<Layout>
					<Header>
						<h1 style={{ textAlign: 'center' }}>欢迎来到教务管理系统</h1>
					</Header>
					<Layout style={{ padding: '0 24px' }}>
						<Breadcrumb style={{ margin: '16px 0' }}>
							<Breadcrumb.Item>Home</Breadcrumb.Item>
							<Breadcrumb.Item>List</Breadcrumb.Item>
							<Breadcrumb.Item>App</Breadcrumb.Item>
						</Breadcrumb>
						<Content>{children}</Content>
						<Footer>Footer</Footer>
					</Layout>
				</Layout>
			</Layout>
		</>
	);
};
export default Student;
