"{
        ""firebase_toke"":""firebase_token"",
        ""group_id"":1,
        ""role_id"": 1,
        ""updated_user_id"":""67246a71-c66d-11e9-a75d-42010a4db004""
}"	"{
    ""http_code"": ""202""
}

{
    ""http_code"": ""404"",
    ""error"": ""No user with this user_id or user has registered!!!""
}"	"Điều kiện: request user = group.creater & key( user_id, group_id) tìm thấy user_group_role 
Chỉ có thể update về role_id=3 hoặc 4( MEMBER or FOLLOWER) khi role_id đang là 2,3,5( ..., REQUEST)
Update user_group_role.role_id theo key ( group_id & user_id)"