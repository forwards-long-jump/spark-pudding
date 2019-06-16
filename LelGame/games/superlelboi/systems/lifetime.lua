function getRequiredComponents()
	return {"lifetime"}
end

function update()
	for i, entity in ipairs(entities) do
		entity.lifetime.tick = entity.lifetime.tick - 1
		if entity.lifetime.tick < 0 then
			entity._meta:delete()
		end
	end
end
