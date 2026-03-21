# Model Context Protocol (MCP)

## Overview

MCP (Model Context Protocol) is a standardized way for AI agents (LLMs) to communicate with external tools and services.

It solves the problem of non-standard integrations between AI agents and external systems.

---

## Table of Contents

- [Basic Terminology](#basic-terminology)
- [Communication Protocol](#communication-protocol)
- [Key Characteristics](#key-characteristics)
- [Problem MCP Solves](#problem-mcp-solves)
- [Data Transfer Mechanisms](#data-transfer-mechanisms)
- [Roles](#roles)
- [SDK Support](#sdk-support)
- [MCP Server Capabilities](#mcp-server-capabilities)
- [MCP Client Capabilities](#mcp-client-capabilities)

---

## Basic Terminology

- **Model** → LLM  
- **Context** → Provides context to the LLM about capability of the server  
- **Protocol** → Standard way of communicating the context between client and server  

---

## Communication Protocol

Protocol used is **JSON-RPC**

- **JSON** → client and server exchange JSON  
- **RPC (Remote Procedure Call)** → client and server execute remote procedure calls on each other  

---

## Key Characteristics

- MCP is **stateful**  
- MCP is **bi-directional**  

---

## Problem MCP Solves

AI agents need to connect to external tools to perform tasks.

### Before MCP:
- No standardization  
- Separate code had to be written for each tool  
- Each tool had its own way of defining:
  - Endpoints  
  - Authentication  
  - Communication  

This made integrations messy and not scalable.

### With MCP:
- Introduces a **standardized way** for servers to expose functionalities  
- Allows AI agents to interact with multiple tools in a consistent way  

---

## Data Transfer Mechanisms

MCP can use the following protocols to transfer data:

### 1. Stdio
- Used when MCP server and client are on the same machine  

### 2. HTTP
- Used when MCP server and client are on different machines  

---

## Roles

### Server
- Any service that wants to expose its functionalities to an LLM  

### Client
- The AI agent  
- Examples:
  - Windsurf  
  - Claude Desktop  

---

## SDK Support

SDKs are available to build:
- MCP clients  
- MCP servers  

---

## MCP Server Capabilities

MCP server exposes:

### 1. Tools
- Functionalities that the server provides  
- Example: Provisioning a VM  

### 2. Resources
- Read-only metadata or information that the server provides  

### 3. Prompts
- Detailed prompts that the agent can use to feed into the LLM  

---

## MCP Client Capabilities

MCP client provides functionalities that server can use:

### 1. Roots
- Server can read certain files on the client’s file system  

### 2. Sampling
- Server can call the LLM via the client  

### 3. Elicitation
- Server can ask more questions / details to the user via the client  

---

## Summary

MCP standardizes how AI agents interact with external tools by:
- Providing a common protocol (JSON-RPC)
- Enabling bi-directional communication
- Reducing custom integration effort
- Making systems more scalable and maintainable
