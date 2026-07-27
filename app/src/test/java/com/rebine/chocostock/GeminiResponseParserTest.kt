package com.rebine.chocostock

import com.rebine.chocostock.data.remote.GeminiApiException
import com.rebine.chocostock.data.remote.GeminiResponseParser
import com.rebine.chocostock.data.remote.InvalidApiKeyException
import org.junit.Assert.assertEquals
import org.junit.Test

class GeminiResponseParserTest {

    private val validResponse = """
        {
          "candidates": [
            {
              "content": {
                "parts": [
                  { "text": "{\"titre\": \"Lindt Excellence 70%\", \"date_peremption\": \"2026-09-15\"}" }
                ]
              }
            }
          ]
        }
    """.trimIndent()

    private val emptyDateResponse = """
        {
          "candidates": [
            {
              "content": {
                "parts": [
                  { "text": "{\"titre\": \"Chocolat mystere\", \"date_peremption\": \"\"}" }
                ]
              }
            }
          ]
        }
    """.trimIndent()

    private val invalidKeyErrorBody = """
    {
      "error": {
        "code": 400,
        "message": "API key not valid. Please pass a valid API key.",
        "status": "INVALID_ARGUMENT"
      }
    }
""".trimIndent()

    private val genericErrorBody = """
    {
      "error": {
        "code": 500,
        "message": "Internal error occurred",
        "status": "INTERNAL"
      }
    }
""".trimIndent()


    private val noCandidatesResponse = """{ "candidates": [] }"""

    @Test
    fun `parse with a valid response extracts title and date`() {
        val result = GeminiResponseParser.parse(validResponse)
        assertEquals("Lindt Excellence 70%", result.title)
        assertEquals("2026-09-15", result.expiryDateIso)
    }

    @Test
    fun `parse with an empty date returns expiryDateIso as null`() {
        val result = GeminiResponseParser.parse(emptyDateResponse)
        assertEquals(null, result.expiryDateIso)
    }

    @Test(expected = IllegalStateException::class)
    fun `parse with no candidate throws an exception`() {
        GeminiResponseParser.parse(noCandidatesResponse)
    }

    @Test(expected = InvalidApiKeyException::class)
    fun `throwForErrorResponse with invalid key message throws InvalidApiKeyException`() {
        GeminiResponseParser.throwForErrorResponse(400, invalidKeyErrorBody)
    }

    @Test(expected = InvalidApiKeyException::class)
    fun `throwForErrorResponse with 403 code throws InvalidApiKeyException`() {
        GeminiResponseParser.throwForErrorResponse(403, genericErrorBody)
    }

    @Test(expected = GeminiApiException::class)
    fun `throwForErrorResponse with generic error throws GeminiApiException`() {
        GeminiResponseParser.throwForErrorResponse(500, genericErrorBody)
    }
}