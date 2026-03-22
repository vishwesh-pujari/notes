# AI Agents, LLMs, and Related Concepts

## Q. Why do we need AI agents and what are they?
**Answer:**
LLMs act as the *brain*—given text, they predict the most likely next token based on training data.

However, LLMs have limitations:
- They **don’t have access to real-time data** (e.g., latest trends, APIs)
- They **don’t know your organization’s private data**
- They are limited by a **finite context window**

To overcome this, we need **AI agents**.

**AI Agents:**
- Systems built around LLMs that can **take actions**
- They **retrieve external data**, **use tools**, and **make decisions**
- They combine:
  - LLM reasoning
  - External tools (APIs, DBs)
  - Memory / context handling

👉 In short:  
**Agents = LLM + Tools + Memory + Control Logic**

---

## Q. What is the currency of LLMs?
**Answer:**
- The currency of LLMs is **tokens**
- Approximation:  
  `1 token ≈ 0.75 words`

- The **context window** (input + output limit) is measured in tokens

---

## Q. What are embeddings?
**Answer:**
- Embeddings are the **vector representation of text**
- Text is converted into **numerical vectors** capturing semantic meaning
- Embeddings vectors have dimensions. For eg. standard is 1500+ dimension vector.

**Why important?**
- Enables **semantic search** instead of keyword matching
- Similar meanings → vectors are closer in space

---

## Q. What are vector databases?
**Answer:**
- Databases optimized to **store and search embeddings**
- Use similarity metrics like **cosine similarity**

**Examples:**
- ChromaDB  
- Pinecone  
- PostgreSQL (with vector extensions like pgvector)

---

## Q. What are chunks and chunk overlapping?
**Answer:**
- Large documents are split into **smaller pieces (chunks)** before embedding

**Why chunking?**
- LLMs and embedding models have **input size limits**
- Improves retrieval granularity

**Chunk Overlapping:**
- Adjacent chunks share some content
- Helps preserve **context continuity**

---

## Q. What are different ways of giving prompts?
**Answer:**

1. **Zero-shot**
   - No examples provided
   - Only instruction

2. **One-shot**
   - One example included
   - Helps define output format

3. **Few-shot**
   - Multiple examples
   - Helps model generalize patterns

4. **Chain-of-Thought (CoT)**
   - Encourages step-by-step reasoning
   - Improves performance on complex tasks

---

## Q. What is RAG?
**Answer:**
**RAG = Retrieval Augmented Generation**

Flow:
1. **Retrieval**
   - Fetch relevant data from vector DB

2. **Augmentation**
   - Add retrieved data to the prompt

3. **Generation**
   - LLM generates answer using enriched context

👉 Enables LLMs to answer using **external + up-to-date + private data**

---

## Q. What is LangChain?
**Answer:**
A framework (primarily Python/JS) that simplifies building LLM applications.

**Key features:**
1. Easy integration with multiple LLM providers
2. Prompt management using **PromptTemplates**
3. Output parsing utilities
4. Workflow chaining (sequential pipelines)

---

## Q. What is LangGraph?
**Answer:**
An extension of LangChain for building **stateful, multi-step AI workflows**

**Key concepts:**
- **Nodes** → functions (tasks)
- **Edges** → transitions (including conditionals)
- **State** → shared data across steps

👉 Useful for:
- Complex decision-making flows
- Agent-like systems with branching logic

---

## Q. What is MCP (Model Context Protocol)?
**Answer:**
- A protocol that allows AI agents to **connect with external tools and services in a standardized way**

**Key characteristics:**
- Uses **JSON-RPC** for communication
- Supports **bi-directional communication**
- Enables agents to:
  - Call tools
  - Fetch data
  - Execute actions

👉 MCP standardizes how:
- LLMs discover tools
- LLMs invoke tools
- Systems provide context to LLMs

---

## Summary

| Concept        | Purpose |
|---------------|--------|
| LLM           | Core reasoning engine |
| Tokens        | Unit of input/output |
| Embeddings    | Semantic representation |
| Vector DB     | Store & retrieve meaning-based data |
| Chunking      | Break large data for processing |
| RAG           | Inject external knowledge into LLM |
| LangChain     | Simplify LLM pipelines |
| LangGraph     | Build complex agent workflows |
| MCP           | Standardized tool interaction |

---
