# coding=utf-8

import requests

# 服务器地址（根据你本地运行的情况调整）
SERVER_URL = "http://localhost:80"  # 或者 http://127.0.0.1:80

def get_resource_info(url_path: str):
    """向逻辑服务器请求资源信息"""
    full_url = f"{SERVER_URL}/{url_path.lstrip('/')}"
    print(f"请求 URL: {full_url}")

    try:
        response = requests.get(full_url)#, timeout=5)
        response.raise_for_status()
        data = response.json()
        print("响应数据:")
        print(data)
    except requests.exceptions.RequestException as e:
        print(f"请求失败: {e}")
    except ValueError:
        print("返回的不是JSON，可能是服务器错误或返回HTML")

if __name__ == "__main__":
    # 测试1: 获取某个文件夹信息
    get_resource_info("C/AI_test/test_data/test_A")

    # 测试2: 获取单个文件信息
    get_resource_info("C/AI_test/dog.jpg")

    get_resource_info("D/Zeno/test_vdb")