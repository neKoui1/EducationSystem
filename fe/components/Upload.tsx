import React from 'react';
const Upload = ({
	children,
	url,
}: {
	children: React.ReactNode;
	url: string;
}) => {
	const handleUpload = async (e: React.MouseEvent<HTMLInputElement>) => {
		const file = await window.showOpenFilePicker({
			types: [
				{
					accept: {
						'application/*': ['.xlxs', '.xlx'],
					},
				},
			],
			// 可以选择多个图片
			multiple: false,
		});
		let form = new FormData();
		form.append('file', await file[0].getFile());
		await fetch(url, {
			method: 'POST',
			body: form,
		}).then((res) => {
			if (res.ok) {
				console.log('ok');
			}
		});
	};
	return <div onClick={handleUpload}>{children}</div>;
};
export default Upload;
