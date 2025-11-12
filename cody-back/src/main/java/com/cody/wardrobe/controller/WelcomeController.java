package com.cody.wardrobe.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 루트 경로(/) 웰컴 페이지 컨트롤러
 */
@RestController
public class WelcomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcome() {
        return """
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cody API Server</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .container {
            background: white;
            border-radius: 20px;
            padding: 50px;
            max-width: 600px;
            width: 100%;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            text-align: center;
        }

        .status-badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: #10b981;
            color: white;
            padding: 8px 16px;
            border-radius: 50px;
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 20px;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% {
                opacity: 1;
            }
            50% {
                opacity: 0.8;
            }
        }

        .status-dot {
            width: 10px;
            height: 10px;
            background: white;
            border-radius: 50%;
            animation: blink 1.5s infinite;
        }

        @keyframes blink {
            0%, 100% {
                opacity: 1;
            }
            50% {
                opacity: 0.3;
            }
        }

        h1 {
            font-size: 48px;
            color: #1f2937;
            margin-bottom: 10px;
            font-weight: 800;
        }

        .subtitle {
            font-size: 20px;
            color: #6b7280;
            margin-bottom: 40px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .info-card {
            background: #f9fafb;
            padding: 20px;
            border-radius: 12px;
            border: 1px solid #e5e7eb;
        }

        .info-label {
            font-size: 12px;
            color: #9ca3af;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 5px;
        }

        .info-value {
            font-size: 18px;
            color: #1f2937;
            font-weight: 600;
        }

        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 14px 28px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            font-size: 16px;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #667eea;
            color: white;
            transform: translateY(-2px);
        }

        .icon {
            width: 20px;
            height: 20px;
        }

        .endpoints {
            margin-top: 40px;
            padding-top: 40px;
            border-top: 1px solid #e5e7eb;
        }

        .endpoints-title {
            font-size: 16px;
            color: #6b7280;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .endpoint-list {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .endpoint-item {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px 15px;
            background: #f9fafb;
            border-radius: 8px;
            font-size: 14px;
            color: #4b5563;
            text-decoration: none;
            transition: all 0.2s ease;
        }

        .endpoint-item:hover {
            background: #f3f4f6;
            transform: translateX(5px);
        }

        .endpoint-method {
            font-weight: 700;
            color: #10b981;
            min-width: 40px;
        }

        .footer {
            margin-top: 40px;
            color: #9ca3af;
            font-size: 14px;
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h1 {
                font-size: 36px;
            }

            .subtitle {
                font-size: 16px;
            }

            .btn-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="status-badge">
            <span class="status-dot"></span>
            SERVER RUNNING
        </div>

        <h1>🎨 Cody API</h1>
        <p class="subtitle">스마트 옷장 관리 서비스</p>

        <div class="info-grid">
            <div class="info-card">
                <div class="info-label">PORT</div>
                <div class="info-value">8080</div>
            </div>
            <div class="info-card">
                <div class="info-label">VERSION</div>
                <div class="info-value">v1.0</div>
            </div>
            <div class="info-card">
                <div class="info-label">FRAMEWORK</div>
                <div class="info-value">Spring Boot</div>
            </div>
        </div>

        <div class="btn-group">
            <a href="/swagger-ui.html" class="btn btn-primary">
                <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
                API 문서 (Swagger)
            </a>
            <a href="/actuator/health" class="btn btn-secondary">
                <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
                Health Check
            </a>
        </div>

        <div class="endpoints">
            <div class="endpoints-title">📍 주요 API 엔드포인트</div>
            <div class="endpoint-list">
                <div class="endpoint-item">
                    <span class="endpoint-method">GET</span>
                    <span>/api/items</span>
                </div>
                <div class="endpoint-item">
                    <span class="endpoint-method">GET</span>
                    <span>/api/outfits</span>
                </div>
                <div class="endpoint-item">
                    <span class="endpoint-method">GET</span>
                    <span>/api/histories</span>
                </div>
                <div class="endpoint-item">
                    <span class="endpoint-method">GET</span>
                    <span>/api/outfits/recommend</span>
                </div>
            </div>
        </div>

        <div class="footer">
            <p>🚀 Cody Backend Server is ready to accept requests</p>
            <p style="margin-top: 5px; font-size: 12px;">
                Made with ❤️ by Cody Development Team
            </p>
        </div>
    </div>

    <script>
        // API 상태 실시간 확인
        fetch('/actuator/health')
            .then(response => response.json())
            .then(data => {
                console.log('✅ Server Health:', data);
            })
            .catch(error => {
                console.warn('⚠️ Health check failed:', error);
            });
    </script>
</body>
</html>
                """;
    }
}

