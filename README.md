# MCP Gateway Lab (企業級數據面 - 聚合模式)

這個實驗室展示了 **MCP Gateway (聚合網關)**。它在企業級架構中扮演「數據面」的角色。

## 核心職責
- **通訊聚合**：對接多個 MCP Sidecar 並彙整工具清單。
- **請求路由**：將 Agent 的工具呼叫請求精確轉發至對應的 Sidecar。
- **單一入口**：為前端 Agent 提供統一的 MCP 端點。

## 專案結構
- **/mcp-gateway**: Java/Spring Boot 網關 (Port 8080)
- **/k8s**: 部署至 Kubernetes 的設定檔

## 與 Registry 的協作
與之前的 Central Gateway 不同，這個版本的 Gateway 會與 **MCP Registry** 互動，動態獲得目前的工具拓撲。
