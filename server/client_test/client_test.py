# coding=utf-8

import requests
import json

# =========================================================
# 配置区
# =========================================================
SERVER_IP = "192.168.3.104"    # ⚠️ 改成你的服务器IP（局域网时改成 192.168.x.x）
SERVER_PORT = 80
BASE_URL = f"http://{SERVER_IP}:{SERVER_PORT}"

# =========================================================
# 工具函数
# =========================================================
def pretty_print_json(data):
    """美化打印JSON"""
    print(json.dumps(data, indent=2, ensure_ascii=False))

def get_resource(url_path: str):
    """请求服务器的资源信息"""
    full_url = f"{BASE_URL}/{url_path.lstrip('/')}"
    print(f"\n=== 请求 URL ===\n{full_url}")

    try:
        resp = requests.get(full_url)#, timeout=5)
        resp.raise_for_status()

        # 尝试解析为JSON
        try:
            data = resp.json()
            print("\n=== 响应(JSON) ===")
            pretty_print_json(data)
        except ValueError:
            # 如果不是JSON，打印前200字节看看
            print("\n⚠️ 响应内容非JSON，前200字节：")
            print(resp.text[:200])

    except requests.exceptions.RequestException as e:
        print(f"\n❌ 请求失败: {e}")

# =========================================================
# 主流程
# =========================================================
if __name__ == "__main__":
    print("=== Logic Server 测试客户端 ===")

    # 示例1：访问逻辑关键字根目录（聚合目录）
    get_resource("国产妹子")

    # 示例2：访问子目录
    get_resource("国产妹子/杨雪欣")

    # 示例3：访问单个文件（图片、视频等）
    #get_resource("风景图/A类图片/IMG_0001.jpg")

    # 示例4：访问不存在路径（测试异常处理）
    #get_resource("不存在的关键字/测试")