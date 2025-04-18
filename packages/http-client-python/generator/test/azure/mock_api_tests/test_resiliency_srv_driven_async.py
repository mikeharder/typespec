# -------------------------------------------------------------------------
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License. See License.txt in the project root for
# license information.
# --------------------------------------------------------------------------
import pytest
from resiliency.srv.driven1.aio import ResiliencyServiceDrivenClient as V1Client
from resiliency.srv.driven2.aio import ResiliencyServiceDrivenClient as V2Client


def get_v1_client(service_deployment_version: str, api_version: str = "v1") -> V1Client:
    return V1Client(
        endpoint="http://localhost:3000",
        service_deployment_version=service_deployment_version,
        api_version=api_version,
    )


def get_v2_client(service_deployment_version: str, api_version: str = "v2") -> V2Client:
    return V2Client(
        endpoint="http://localhost:3000",
        service_deployment_version=service_deployment_version,
        api_version=api_version,
    )


@pytest.mark.asyncio
async def test_add_optional_param_from_none():
    # old client to old service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v1") as client:
        await client.from_none()

    # old client to new service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_none()

    # new client to new service with api version v1
    async with V2Client(
        endpoint="http://localhost:3000",
        service_deployment_version="v2",
        api_version="v1",
    ) as client:
        await client.from_none()

    # new client to new service with api version v2
    async with V2Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_none(new_parameter="new")


@pytest.mark.asyncio
async def test_add_optional_param_from_one_required():
    # old client to old service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v1") as client:
        await client.from_one_required(parameter="required")

    # old client to new service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_one_required(parameter="required")

    # new client to new service with api version v1
    async with V2Client(
        endpoint="http://localhost:3000",
        service_deployment_version="v2",
        api_version="v1",
    ) as client:
        await client.from_one_required(parameter="required")

    # new client to new service with api version v2
    async with V2Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_one_required(parameter="required", new_parameter="new")


@pytest.mark.asyncio
async def test_add_optional_param_from_one_optional():
    # old client to old service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v1") as client:
        await client.from_one_optional(parameter="optional")

    # old client to new service with api version v1
    async with V1Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_one_optional(parameter="optional")

    # new client to new service with api version v1
    async with V2Client(
        endpoint="http://localhost:3000",
        service_deployment_version="v2",
        api_version="v1",
    ) as client:
        await client.from_one_optional(parameter="optional")

    # new client to new service with api version v2
    async with V2Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.from_one_optional(parameter="optional", new_parameter="new")


@pytest.mark.asyncio
async def test_break_the_glass():
    from azure.core.rest import HttpRequest

    request = HttpRequest(method="DELETE", url="/add-operation")
    async with V1Client(
        endpoint="http://localhost:3000",
        service_deployment_version="v2",
        api_version="v2",
    ) as client:
        response = await client.send_request(request)
        response.raise_for_status()


@pytest.mark.asyncio
async def test_add_operation():
    async with V2Client(endpoint="http://localhost:3000", service_deployment_version="v2") as client:
        await client.add_operation()


@pytest.mark.parametrize(
    "func_name, params",
    [
        ("from_none", {"new_parameter": "new"}),
        ("from_one_optional", {"parameter": "optional", "new_parameter": "new"}),
        ("from_one_required", {"parameter": "required", "new_parameter": "new"}),
        ("add_operation", {}),
    ],
)
@pytest.mark.asyncio
async def test_new_client_with_old_apiversion_call_new_parameter(func_name, params):
    client = get_v2_client(service_deployment_version="v2", api_version="v1")
    with pytest.raises(ValueError) as ex:
        await getattr(client, func_name)(**params)
    assert "is not available in API version" in str(ex.value)
